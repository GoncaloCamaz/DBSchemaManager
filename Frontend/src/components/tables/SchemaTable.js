import React, { useState } from 'react'
import { Paper, makeStyles, TableBody, TableRow, TableCell, Toolbar, InputAdornment } from '@material-ui/core';
import useTable from "./useTable";
import Controls from "../controls/Controls";
import { Search } from "@material-ui/icons";
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import OpenInBrowserIcon from '@material-ui/icons/OpenInBrowser';
import VpnKeyIcon from '@material-ui/icons/VpnKey';
import UpdateIcon from '@material-ui/icons/Update';
import '../../pageutils/Button.css'

const useStyles = makeStyles(theme => ({
    pageContent: {
        overflowX: 'scroll',
        overflowY: 'scroll',
        maxHeight: '90%',
        width: '100%',
        margin: theme.spacing(0),
        padding: theme.spacing(3)
    },
    container: {
        maxHeight: '300px',
    },
    searchInput: {
        width: '30%',
    },
    newButton: {
        position: 'absolute',
        right: 0,
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

const headCells = [
    { id: 'name', label: 'Name' },
    { id: 'sqlservername', label: 'Server' },
    { id: 'updateperiod', label: 'Update Period'},
    { id: 'lastupdate', label: 'Last Update'},
    { id: 'description', label: 'Description' },    
    { id: 'actions', label: 'Actions', disableSorting: true }
]

export default function SchemaTable(props) {
    const classes = useStyles();
    const records = props.rows
    const [filterFn, setFilterFn] = useState({ fn: items => { return items; } })
    const userRole = localStorage.getItem('userrole')
  
    const {
        TblContainer,
        TblHead,
        TblPagination,
        recordsAfterPagingAndSorting
    } = useTable(records, headCells, filterFn);

    const handleSearch = e => {
        let target = e.target;
        setFilterFn({
            fn: items => {
                if (target.value === null || target.value === '' || target.value === ' ')
                    return items;
                else
                {
                    let filtered = items.filter(value => {
                        return (
                            value.name.toLowerCase().includes(target.value.toLowerCase()) ||
                            value.sqlservername.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.updateperiod.toString().toLowerCase().includes(target.value.toLowerCase())
                        );
                    })
                    return filtered      
                }
            }
        })
    }

    const openInPopup = item => {
        props.openInPopup(item)
    }

    const openInPopupRemove = item => {
        props.openInPopupRemove(item)
    }

    const sendAQuery = item => {
        props.sendAQuery(item)
    }

    const openEditCredentialsPopup = item => {
        props.openEditCredentialsPopup(item)
    }

    const startOnDemandUpdate = item => {
        props.startOnDemandUpdate(item)
    }

    if(userRole === 'ADMIN')
    {
        return (
            <Paper className={classes.pageContent}>
                <Toolbar>
                    <Controls.Input
                        label="Search"
                        className={classes.searchInput}
                        InputProps={{
                            startAdornment: (<InputAdornment position="start">
                                <Search />
                            </InputAdornment>)
                        }}
                        onChange={handleSearch}
                    />
                    <Controls.Button
                        text="Add New Schema"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={() => {openInPopup(null)}}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.name}</TableCell>
                                    <TableCell>{item.sqlservername}</TableCell>
                                    <TableCell>{item.updateperiod}</TableCell>
                                    <TableCell>{item.lastupdate}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Send a Query"
                                            className={classes.editButton}
                                            onClick={() => { sendAQuery(item) }}>
                                            <OpenInBrowserIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Schema Credentials"
                                            className={classes.editButton}
                                            onClick={() => { openEditCredentialsPopup(item) }}>
                                            <VpnKeyIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Update Schema"
                                            className={classes.editButton}
                                            onClick={() => { startOnDemandUpdate(item) }}>
                                            <UpdateIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Edit Schema"
                                            className={classes.editButton}
                                            onClick={() => { openInPopup(item) }}>
                                            <EditIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Remove Schema"
                                            className={classes.removeButton}
                                            onClick={() => { openInPopupRemove(item) }}>
                                            <DeleteIcon fontSize="small" />
                                        </Controls.ActionButton>
                                    </TableCell>
                                </TableRow>)
                            })
                        }
                    </TableBody>
                </TblContainer>
                <TblPagination />
            </Paper>
        )
    }
    else if(userRole === 'USER')
    {
        return (
            <Paper className={classes.pageContent}>
                <Toolbar>
                    <Controls.Input
                        label="Search"
                        className={classes.searchInput}
                        InputProps={{
                            startAdornment: (<InputAdornment position="start">
                                <Search />
                            </InputAdornment>)
                        }}
                        onChange={handleSearch}
                    />
                    <Controls.Button
                        text="Add New Schema"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={() => {openInPopup(null)}}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                            <TableCell>{item.name}</TableCell>
                                            <TableCell>{item.sqlservername}</TableCell>
                                            <TableCell>{item.updateperiod}</TableCell>
                                            <TableCell>{item.lastupdate}</TableCell>
                                            <TableCell>{item.description}</TableCell>
                                            <TableCell>
                                                <Controls.ActionButton
                                                    color="primary"
                                                    title="Send a Query"
                                                    className={classes.editButton}
                                                    onClick={() => { sendAQuery(item) }}>
                                                    <OpenInBrowserIcon fontSize="small" />
                                                </Controls.ActionButton>
                                                <Controls.ActionButton
                                                    color="primary"
                                                    title="Update Schema"
                                                    className={classes.editButton}
                                                    onClick={() => { startOnDemandUpdate(item) }}>
                                                    <UpdateIcon fontSize="small" />
                                                </Controls.ActionButton>
                                                <Controls.ActionButton
                                                    color="primary"
                                                    title="Edit Schema"
                                                    className={classes.editButton}
                                                    onClick={() => { openInPopup(item) }}>
                                                    <EditIcon fontSize="small" />
                                                </Controls.ActionButton>
                                            </TableCell>
                                        </TableRow>
                                )
                            })
                        }
                    </TableBody>
                </TblContainer>
                <TblPagination />
            </Paper>
        )
    }
    else{
        return (
            <Paper className={classes.pageContent}>
                <Toolbar>
                    <Controls.Input
                        label="Search"
                        className={classes.searchInput}
                        InputProps={{
                            startAdornment: (<InputAdornment position="start">
                                <Search />
                            </InputAdornment>)
                        }}
                        onChange={handleSearch}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                            <TableCell>{item.name}</TableCell>
                                            <TableCell>{item.sqlservername}</TableCell>
                                            <TableCell>{item.updateperiod}</TableCell>
                                            <TableCell>{item.lastupdate}</TableCell>
                                            <TableCell>{item.description}</TableCell>
                                        </TableRow>
                                    )
                            })
                        }
                    </TableBody>
                </TblContainer>
                <TblPagination />
            </Paper>
        )
    }
}
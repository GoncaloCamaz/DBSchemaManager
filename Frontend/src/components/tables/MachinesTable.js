import React, { useState } from 'react'
import { Paper, makeStyles, TableBody, TableRow, TableCell, Toolbar, InputAdornment } from '@material-ui/core';
import useTable from "./useTable";
import Controls from "../controls/Controls";
import { Search } from "@material-ui/icons";
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import WebIcon from '@material-ui/icons/Web';
import AccountBoxIcon from '@material-ui/icons/AccountBox';
import '../../pageutils/Button.css'

const useStyles = makeStyles(theme => ({
    pageContent: {
        overflowX: 'scroll',
        overflowY: 'scroll',
        maxHeight: '90%',
        width: '100%',
        margin: theme.spacing(0),
        padding: theme.spacing(3),
    },
    container: {
        maxHeight: 450,
    },
    searchInput: {
        width: '30%'
    },
    newButton: {
        position: 'absolute',
        right: '10px',
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

const headCells = [
    { id: 'name', label: 'Name' },
    { id: 'machinetype', label: 'Type' },
    { id: 'serviceip', label: 'Service IP' },
    { id: 'managementip', label: 'Management IP' },
    { id: 'operativesystem', label: 'OS' },
    { id: 'observations', label: 'Observations' },
    { id: 'description', label: 'Description' },
    { id: 'actions', label: 'Actions', disableSorting: true }
]

export default function MachinesTable(props) {
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
                            value.machinetype.toLowerCase().includes(target.value.toLowerCase()) ||
                            value.operativesystem.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.serviceip.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.managementip.toString().toLowerCase().includes(target.value.toLowerCase())
                          );
                    })
                    return filtered
                }
            }
        })
    }

    const openAssociatedAccounts = item => {
        props.openAssociatedAccounts(item)
    }

    const openPlatformsByMachine = item => {
        props.openPlatformsByMachine(item)
    }

    const openInPopup = item => {
        props.openInPopup(item)
    }

    const openAddPopup = () => {
        props.openAddPopup()
    }

    const openInPopupRemove = item => {
        props.openInPopupRemove(item)
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
                        text="Add New Machine"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={openAddPopup}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead/>
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.name}</TableCell>
                                    <TableCell>{item.machinetype}</TableCell>
                                    <TableCell>{item.serviceip}</TableCell>
                                    <TableCell>{item.managementip}</TableCell>
                                    <TableCell>{item.operativesystem}</TableCell>
                                    <TableCell>{item.observations}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell>
                                    <Controls.ActionButton
                                            color="primary"
                                            title="Associated Accounts"
                                            className={classes.editButton}
                                            onClick={() => { openAssociatedAccounts(item) }}>
                                            <AccountBoxIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Associated Platforms"
                                            className={classes.editButton}
                                            onClick={() => { openPlatformsByMachine(item) }}>
                                            <WebIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Edit Machine"
                                            className={classes.editButton}
                                            onClick={() => { openInPopup(item) }}>
                                            <EditIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Remove Machine"
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
                        text="Add New Machine"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={openAddPopup}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.name}</TableCell>
                                    <TableCell>{item.machinetype}</TableCell>
                                    <TableCell>{item.serviceip}</TableCell>
                                    <TableCell>{item.managementip}</TableCell>
                                    <TableCell>{item.operativesystem}</TableCell>
                                    <TableCell>{item.observations}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Associated Platforms"
                                            className={classes.editButton}
                                            onClick={() => { openPlatformsByMachine(item) }}>
                                            <WebIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Edit Machine"
                                            className={classes.editButton}
                                            onClick={() => { openInPopup(item) }}>
                                            <EditIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Remove Machine"
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
    else
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
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.name}</TableCell>
                                    <TableCell>{item.machinetype}</TableCell>
                                    <TableCell>{item.serviceip}</TableCell>
                                    <TableCell>{item.managementip}</TableCell>
                                    <TableCell>{item.operativesystem}</TableCell>
                                    <TableCell>{item.observations}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Associated Platforms"
                                            className={classes.editButton}
                                            onClick={() => { openPlatformsByMachine(item) }}>
                                            <WebIcon fontSize="small" />
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
}
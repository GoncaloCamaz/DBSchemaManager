import React, { useState } from 'react'
import { Paper, makeStyles, TableBody, TableRow, TableCell, Toolbar, InputAdornment } from '@material-ui/core';
import useTable from "./useTable";
import Controls from "../controls/Controls";
import { Search } from "@material-ui/icons";
import DeleteIcon from '@material-ui/icons/Delete';

const useStyles = makeStyles(theme => ({
    pageContent: {
        overflowX: 'scroll',
        overflowY: 'auto',
        maxHeight: '90%',
        width: '100%',
        margin: theme.spacing(0),
        padding: theme.spacing(3)
    },
    container: {
        maxHeight: 450,
    },
    searchInput: {
        width: '30%',
    },
    newButton: {
        position: 'relative',
        left: '55%',
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

const headCells = [
    { id: 'machinename', label: 'Name' },
    { id: 'machinetype', label: 'Type' },
    { id: 'machineserviceip', label: 'Service IP' },
    { id: 'machinemanagementip', label: 'Management IP' },
    { id: 'actions', label: 'Actions', disableSorting: true }
]

export default function MachinesByPlatformTable(props) {
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
                            value.machinename.toLowerCase().includes(target.value.toLowerCase()) ||
                            value.machineserviceip.toLowerCase().includes(target.value.toLowerCase()) ||
                            value.machinetype.toString().toLowerCase().includes(target.value.toLowerCase())
                          );
                    })
                    return filtered
                }
            }
        })
    }

    const returnToPage = () => 
    {
        props.returnToPage()
    }

    const openInPopup = item => {
        props.openInPopup(item)
    }

    const openInPopupRemove = item => {
        props.openInPopupRemove(item)
    }

    if(userRole === 'ADMIN' || userRole === 'USER')
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
                        text="Add Relation"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={() => {openInPopup(true)}}
                    />
                    <Controls.Button
                        text="Return"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={returnToPage}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.machinename}</TableCell>
                                    <TableCell>{item.machinetype}</TableCell>
                                    <TableCell>{item.machineserviceip}</TableCell>
                                    <TableCell>{item.machinemanagementip}</TableCell>
                                    <TableCell>
                                        <Controls.ActionButton
                                            color="primary"
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
                    <Controls.Button
                        text="Return"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={returnToPage}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.machinename}</TableCell>
                                    <TableCell>{item.machinetype}</TableCell>
                                    <TableCell>{item.machineserviceip}</TableCell>
                                    <TableCell>{item.machinemanagementip}</TableCell>
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
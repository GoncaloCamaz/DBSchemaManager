import React, { useState } from 'react'
import { Paper, makeStyles, TableBody, TableRow, TableCell, Toolbar, InputAdornment } from '@material-ui/core';
import useTable from "./useTable";
import Controls from "../controls/Controls";
import { Search } from "@material-ui/icons";
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
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
        maxHeight: 450,
    },
    searchInput: {
        width: '30%',
        left: 10
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
    { id: 'dbSchemaName', label: 'Schema' },
    { id: 'dbObjectName', label: 'Object' },
    { id: 'dbUsername', label: 'Username' },
    { id: 'permission', label: 'Permission' },
    { id: 'privilege', label: 'Privilege' },
    { id: 'dbSchemaManagerUsername', label: 'DBSchema Manager Username' },
    { id: 'timestamp', label: 'Date' },
    { id: 'description', label: 'Description' },
    { id: 'actions', label: 'Actions', disableSorting: true }
]

export default function AccessesTable(props) {
    const classes = useStyles();
    const schemas = props.schemas
    const firstSearch = schemas[0].id
    const [searchSchema, setSearchSchema] = useState({schema: firstSearch})
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
                            value.dbSchemaName.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.dbObjectName.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.dbUsername.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.privilege.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.dbSchemaManagerUsername.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.timestamp.toString().toLowerCase().includes(target.value.toLowerCase())                        
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

    const handleSelectChange = e => {
        let schema_name = e.target.value
        setSearchSchema({schema: schema_name})
        props.handleSelectSchemaChange(schema_name)
    }

    if(userRole === 'ADMIN')
    {
        return (
            <Paper className={classes.pageContent}>
                <Toolbar>
                    <Controls.Select
                        name="schema"
                        label="Schema"
                        value={searchSchema.schema}
                        onChange={handleSelectChange}
                        options={schemas}
                    />
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
                                    <TableCell>{item.dbSchemaName}</TableCell>
                                    <TableCell>{item.dbObjectName}</TableCell>
                                    <TableCell>{item.dbUsername}</TableCell>
                                    <TableCell>{item.permission}</TableCell>
                                    <TableCell>{item.privilege}</TableCell>
                                    <TableCell>{item.dbSchemaManagerUsername}</TableCell>
                                    <TableCell>{item.timestamp}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Edit Access Description"
                                            className={classes.editButton}
                                            onClick={() => { openInPopup(item) }}>
                                            <EditIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Remove Access"
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
    else if (userRole === 'USER')
    {
        return (
            <Paper className={classes.pageContent}>
                <Toolbar>
                    <Controls.Select
                        className={classes.Select}
                        name="schema"
                        label="Schema"
                        value={searchSchema.schema}
                        onChange={handleSelectChange}
                        options={schemas}
                    />
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
                                    <TableCell>{item.dbSchemaName}</TableCell>
                                    <TableCell>{item.dbObjectName}</TableCell>
                                    <TableCell>{item.dbUsername}</TableCell>
                                    <TableCell>{item.permission}</TableCell>
                                    <TableCell>{item.privilege}</TableCell>
                                    <TableCell>{item.dbSchemaManagerUsername}</TableCell>
                                    <TableCell>{item.timestamp}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Edit Access Description"
                                            className={classes.editButton}
                                            onClick={() => { openInPopup(item) }}>
                                            <EditIcon fontSize="small" />
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
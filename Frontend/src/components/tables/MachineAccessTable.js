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
        width: '30%'
    },
    newButton: {
        position: 'relative',
        left: '45%',
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

const headCells = [
    { id: 'username', label: 'Username' },
    { id: 'password', label: 'Password' },
    { id: 'description', label: 'Description' },
    { id: 'actions', label: 'Actions', disableSorting: true }
]

export default function MachineAccessTable(props) {
    const classes = useStyles();
    const records = props.rows
    const [filterFn, setFilterFn] = useState({ fn: items => { return items; } })
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
                    if(items.filter(x => x.username.toLowerCase().includes(target.value)).length > 0)
                    {
                        return items.filter(x => x.username.toLowerCase().includes(target.value))
                    }
                }
            }
        })
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

    const handleReturn = () => {
        props.handleReturn()
    }

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
                        text="Add New Machine Access"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={openAddPopup}
                    />
                    <Controls.Button
                        text="Return"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={handleReturn}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.username}</TableCell>
                                    <TableCell>{item.password}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Edit Machine Access"
                                            className={classes.editButton}
                                            onClick={() => { openInPopup(item) }}>
                                            <EditIcon fontSize="small" />
                                        </Controls.ActionButton>
                                        <Controls.ActionButton
                                            color="primary"
                                            title="Remove Machine Access"
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
import React, { useState } from 'react'
import { Table, TableHead, TableRow, TableCell, makeStyles, TablePagination, TableSortLabel } from '@material-ui/core'

const useStyles = makeStyles(theme => ({
    root: {
        width: '100%',
      },
    table: {
        marginTop: theme.spacing(1),
        '& thead th': {
            fontWeight: '300',
            color: 'rgb(4, 13, 51)',
            backgroundColor: '#fff',
        },
        '& tbody td': {
            fontWeight: '300',
        },
        '& tbody tr:hover': {
            backgroundColor: '#fff',
            color: '#fff',
            cursor: 'pointer',
        }
    }
}))

export default function useTable(records, headCells,filterFn) {
    const classes = useStyles();
    const pages = [10, 25, 50, 100]
    const [page, setPage] = useState(0)
    const [rowsPerPage, setRowsPerPage] = useState(pages[page])
    const [order, setOrder] = useState()
    const [orderBy, setOrderBy] = useState()

    const TblContainer = props => (
        <Table className={classes.table} stickyHeader>
            {props.children}
        </Table>
    )

    const TblHead = () => {

            const handleSortRequest = cellId => {
                const isAsc = orderBy === cellId && order === "asc";
                setOrder(isAsc ? 'desc' : 'asc');
                setOrderBy(cellId)
            }
    
            return (<TableHead>
                <TableRow>
                    {
                        headCells.map(headCell => (
                            <TableCell key={headCell.id}
                                sortDirection={orderBy === headCell.id ? order : false}>
                                {headCell.disableSorting ? headCell.label :
                                    <TableSortLabel
                                        active={orderBy === headCell.id}
                                        direction={orderBy === headCell.id ? order : 'asc'}
                                        onClick={() => { return handleSortRequest(headCell.id)}}>
                                        {headCell.label}
                                    </TableSortLabel>}
                            </TableCell>))
                    }
                </TableRow>
            </TableHead>)        
    }

    const handleChangePage = (event, newPage) => {
        event.preventDefault()
        setPage(newPage);
    }

    const handleChangeRowsPerPage = event => {
        setRowsPerPage(parseInt(event.target.value, 10))
        setPage(0);
    }

    const TblPagination = () => { return (<TablePagination
        component="div"
        page={page}
        rowsPerPageOptions={pages}
        rowsPerPage={rowsPerPage}
        count={records.length}
        onChangePage={handleChangePage}
        onChangeRowsPerPage={handleChangeRowsPerPage}
    />)}

    function stableSort(array, comparator) {
        const stabilizedThis = array.map((el, index) => [el, index]);
        stabilizedThis.sort((a, b) => {
            const order = comparator(a[0], b[0]);
            if (order !== 0) return order;
            return a[1] - b[1];
        });
        return stabilizedThis.map((el) => el[0]);
    }

    function getComparator(order, orderBy) {
        return order === 'desc'
            ? (a, b) => descendingComparator(a, b, orderBy)
            : (a, b) => -descendingComparator(a, b, orderBy);
    }

    function descendingComparator(a, b, orderBy) {
        if (b[orderBy] < a[orderBy]) {
            return -1;
        }
        if (b[orderBy] > a[orderBy]) {
            return 1;
        }
        return 0;
    }

    const recordsAfterPagingAndSorting = () => {
        return stableSort(filterFn.fn(records), getComparator(order, orderBy))
            .slice(page * rowsPerPage, (page + 1) * rowsPerPage)
    }

    return {
        TblContainer,
        TblHead,
        TblPagination,
        recordsAfterPagingAndSorting
    }
}
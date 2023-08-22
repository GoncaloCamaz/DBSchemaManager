import React from 'react'
import { Button, makeStyles } from '@material-ui/core';

/**
 * CSS to material UI 
 */
const useStyles = makeStyles(theme => ({
    root: {
        minWidth: 0,
        margin: theme.spacing(0.5),
        color: "#1888ff"
    },
    secondary: {
        backgroundColor: "lightred",
        '& .MuiButton-label': {
            color:"rgb(4, 13, 51)",
        }
    },
    primary: {
        backgroundColor: "#fff",
        '& .MuiButton-label': {
            color: 'rgb(4, 13, 51)',
        }
    },
}))

export default function ActionButton(props) {

    const { color, children, onClick, title } = props;
    const classes = useStyles();

    return (
        <Button
            className={`${classes.root} ${classes[color]}`}
            onClick={onClick} title={title}>
            {children}
        </Button>
    )
}

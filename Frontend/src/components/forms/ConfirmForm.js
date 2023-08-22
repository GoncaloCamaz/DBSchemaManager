import React, { useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const useStyles = makeStyles(({
    newButton: {
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#E04232"
          }
    }
}))

/**
 * Confirm form is used to check if an user wants to delete something,
 * showing an warning message adivising that it will delete an object
 * and then, if user confirms, ConfirmForm calls removeFunction from parent component
 * to delete the record. This component is also able to display messages (warning message) that
 * is passed from parent component. The recordForRemove holds the information about the record to be
 * removed
 * @param {*} props from Parent component
 * @returns Form
 */
export default function ConfirmForm(props) {
    const { removeFunction, recordForRemove, warningmessage } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if (fieldValues === values)
            return Object.values(temp).every(x => x === "")
    }

    const {
        values,
        setValues,
        errors,
    } = useForm({}, true, validate);

    const handleSubmit = e => {
        e.preventDefault()
        removeFunction(values)
    }

    useEffect(() => {
        if (recordForRemove != null)
            setValues({
                ...recordForRemove
            })
    // eslint-disable-next-line
    }, [recordForRemove])

    return (
        <Form onSubmit={handleSubmit}>
            <Grid container>
                <Grid item xs={12}>
                    <h3>{warningmessage}</h3>
                    <br/>
                    <br/>
                </Grid>
                <Grid item xs={12}>
                    <Controls.Button
                        className={classes.newButton}
                        type="submit"
                        text="Confirm" 
                        onClick={handleSubmit}
                    />
                </Grid>
            </Grid>
        </Form>
    )
}

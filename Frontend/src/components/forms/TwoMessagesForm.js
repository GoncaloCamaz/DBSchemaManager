import React from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { useForm, Form } from './useForm';
import { makeStyles } from '@material-ui/core';

const useStyles = makeStyles(({
    newButton: {
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

/**
 * Receives message string and handleacknowledge function
 * @param {*} props receives the handleAcknowledge function to be able
 * to call it on parent component. The message that will be displayed on this form is 
 * received from parent component as well.
 */
export default function TwoMessagesForm(props) {
    const { handleOk, mainMessage, secundaryMessage } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if (fieldValues === values)
            return Object.values(temp).every(x => x === "")
    }

    const {
        values,
        errors,
    } = useForm({}, true, validate);

    const handleSubmit = e => {
        e.preventDefault()
        handleOk()
    }

    return (
        <Form onSubmit={handleSubmit}>
            <Grid container>
                <Grid item xs="auto">
                    <div>
                        <h2>{mainMessage}</h2>
                        <br/>
                        <h3>{secundaryMessage}</h3>
                        <br/>
                        <Controls.Button
                            className={classes.newButton}
                            type="submit"
                            text="Ok" 
                            onClick={handleSubmit}
                        />
                    </div>
                </Grid>
            </Grid>
        </Form>
    )
}

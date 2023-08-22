import React from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { Form } from './useForm';

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
 * This form is used to allow the user to override an create view query
 * This popup appears if there are warnings sent from backend while creating a new view
 * 
 * @param {*} props handleOverride to send the content; handleCancel to ignore the operation
 * errormessage has the message to display as does the warning message
 * @returns Form
 */
export default function OverridePermissionForm(props) {
    const { handleOverride, handleCancel ,errormessage, warningmessage } = props
    const classes = useStyles()

    const handleOverridePress = e => {
        e.preventDefault()
        handleOverride()
    }

    const handleCancelPress = e => {
        e.preventDefault()
        handleCancel()
    }

    return (
        <Form >
            <Grid container>
                <Grid item xs="auto">
                    <div>
                    <h2>{errormessage}</h2>
                    <br></br>
                    <h2>{warningmessage}</h2>
                    <br></br>
                    <h3>Press Override to send anyway</h3>
                    <br></br>
                    <Controls.Button
                        className={classes.newButton}
                        type="submit"
                        text="Override" 
                        onClick={handleOverridePress}
                    />
                    <Controls.Button
                        text="Cancel"
                        color="default"
                        onClick={handleCancelPress}
                    />
                    </div>
                </Grid>
            </Grid>
        </Form>
    )
}

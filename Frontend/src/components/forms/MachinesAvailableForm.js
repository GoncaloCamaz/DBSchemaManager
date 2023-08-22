import React from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const initialFValues = {
    machine: ''
}

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
 * Form used to show all available machines not used from a given platform yet
 * @param {*} props receives add or edit function and the list of machines
 * @returns 
 */
export default function MachinesAvailableForm(props) {
    const { addOrEdit, machines } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('machine' in fieldValues)
            temp.machine = fieldValues.machine ? "" : "This field is required."
        setErrors({
            ...temp
        })
        if (fieldValues === values)
            return Object.values(temp).every(x => x === "")
    }

    const {
        values,
        errors,
        setErrors,
        handleInputChange,
        resetForm
    } = useForm(initialFValues, true, validate);

    const handleSubmit = e => {
        e.preventDefault()
        if (validate()) {
            addOrEdit(values, resetForm);
        }
    }

    return (
        <Form onSubmit={handleSubmit}>
            <Grid container spacing={5}>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                    <Controls.Select
                        name="machine"
                        label="Unused Available Machines"
                        multiple={false}
                        value={values.machine}
                        onChange={handleInputChange}
                        options={machines}
                        error={errors.machine}
                    />
                </Grid>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                    <div>
                        <Controls.Button
                            className={classes.newButton}
                            type="submit"
                            text="Confirm" 
                            onClick={handleSubmit}/>
                        <Controls.Button
                            text="Reset"
                            color="default"
                            onClick={resetForm} />
                    </div>
                </Grid>
            </Grid>
        </Form>
    )
}

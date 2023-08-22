import React, { useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const serverItems = [
    { id: 'MySQL', title: 'MySQL' },
    { id: 'SQLServer', title: 'SQLServer' },
    { id: 'Oracle', title: 'Oracle' },
    { id: 'PostgreSQL', title: 'PostgreSQL' }
]

const updatePediodItems = [
    { id: 'Daily', title: 'Daily' },
    { id: 'Weekly', title: 'Weekly'},
    { id: 'Monthly', title: 'Monthly' }
]

const initialFValues = {
    name: '',
    connectionstring: '',
    username: '',
    password: '',
    updateperiod: 'Daily',
    sqlservername: 'MySQL',
    description: '',
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

export default function SchemaEditForm(props) {
    const { addOrEdit, recordForEdit } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('name' in fieldValues)
            temp.name = fieldValues.name ? "" : "This field is required."
        if ('connectionstring' in fieldValues)
            temp.connectionstring = fieldValues.connectionstring ? "" : "This field is required."
        if ('username' in fieldValues)
            temp.username = fieldValues.username ? "" : "This field is required."
        if ('password' in fieldValues)
            temp.password = fieldValues.password ? "" : "This field is required."
        if ('sqlservername' in fieldValues)
            temp.sqlservername = fieldValues.sqlservername ? "" : "This field is required."
        if ('updateperiod' in fieldValues)
            temp.updateperiod = fieldValues.updateperiod ? "" : "This field is required." 
        setErrors({
            ...temp
        })
        if (fieldValues === values)
            return Object.values(temp).every(x => x === "")
    }

    const {
        values,
        setValues,
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

    useEffect(() => {
        if (recordForEdit != null)
            setValues({
                ...recordForEdit
            })
    // eslint-disable-next-line
    }, [recordForEdit])

    return (
        <Form onSubmit={handleSubmit}>
            <Grid container>
                <Grid item lg={6} md={6} sm={12} xs={12}>
                    <Controls.Input
                        name="name"
                        label="Schema Name"
                        value={values.name}
                        onChange={handleInputChange}
                        error={errors.name}
                    />
                    <Controls.Input
                        label="Description"
                        name="description"
                        value={values.description}
                        onChange={handleInputChange}
                    />
                </Grid>
                <Grid item lg={6} md={6} sm={12} xs={12}>
                    <Controls.RadioGroup
                        name="sqlservername"
                        label="Server"
                        value={values.sqlservername}
                        onChange={handleInputChange}
                        checked={values.sqlservername}
                        items={serverItems}
                        error={errors.sqlservername}
                    />
                    <Controls.RadioGroup
                        name="updateperiod"
                        label="Update Period"
                        value={values.updateperiod}
                        onChange={handleInputChange}
                        checked={values.updateperiod}
                        items={updatePediodItems}
                        error={errors.updateperiod}
                    />
                    <div>
                        <Controls.Button
                            className={classes.newButton}
                            type="submit"
                            text="Update" 
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

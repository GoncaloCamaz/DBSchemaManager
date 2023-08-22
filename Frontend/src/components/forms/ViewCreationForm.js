import React, { useEffect } from 'react'
import { Grid } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const useStyles = makeStyles(({
    newButton: {
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

const initialFValues = {
    name: '',
    username: '',
    password: '',
    script: '',
}

export default function ViewCreationForm(props) {
    const classes = useStyles()
    const { addOrEdit, recordForEdit, schemas, handleSelectSchemaChange, scriptfromhistory, currentSchema } = props
    
    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if('name' in fieldValues)
            temp.name = fieldValues.name ? "" : "This field is required."
        if ('script' in fieldValues)
            temp.script = fieldValues.script ? "" : "This field is required."
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

    const handleSelectSchema = e => {
        e.preventDefault()
        handleInputChange(e)
        handleSelectSchemaChange(e.target.value)
    }

    const handleSchemaFromHistory = item => {
        handleSelectSchemaChange(item)
    }

    useEffect(() => {
        if (recordForEdit != null && scriptfromhistory == null)
            setValues({
                username: recordForEdit.username,
                password: recordForEdit.password,
                name: recordForEdit.name,
                script: recordForEdit.script
            })
        else if (recordForEdit != null && scriptfromhistory != null)
        {
            setValues({
                username: recordForEdit.username,
                password: recordForEdit.password,
                name: currentSchema,
                script: scriptfromhistory
            })
            handleSchemaFromHistory(currentSchema)
        }
    // eslint-disable-next-line
    }, [recordForEdit])

    return (
        <Form onSubmit={handleSubmit}>
            <Grid container spacing={3}>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                    <Controls.Select
                        name="name"
                        label="Select Schema"
                        options={schemas}
                        onChange={handleSelectSchema}
                        value={values.name}
                        error={errors.name}
                    />
                    <Controls.Input
                        label="Optional Schema Username"
                        name="username"
                        value={values.username}
                        onChange={handleInputChange}
                    />
                    <Controls.Input
                        label="Optional Schema Password"
                        name="password"
                        value={values.password}
                        onChange={handleInputChange}
                    />
                    <Controls.Input
                        label="View Code"
                        name="script"
                        multiline={true}
                        rowsMax="12"
                        value={values.script}
                        error={errors.script}
                        onChange={handleInputChange}
                    />
                </Grid>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                    <Controls.Button
                        className={classes.newButton}
                        type="submit"
                        text="Confirm" 
                        onClick={handleSubmit}
                    /> 
                    <Controls.Button
                        text="Reset"
                        color="default"
                        onClick={resetForm} />
                </Grid>
                <Grid item xs={3}>
                </Grid>
                <Grid item xs={3}>
                </Grid>
                <Grid item xs={3}>
                </Grid>
                <Grid item xs={3}>
                </Grid>        
            </Grid>
        </Form>
    )
}
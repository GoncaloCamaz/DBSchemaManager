import React, { useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const initialFValues = {
    connectionstring: '',
    username: '',
    password: ''
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

export default function SchemaCredentialsForm(props) {
    const { addOrEdit, recordForEdit } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('connectionstring' in fieldValues)
            temp.connectionstring = fieldValues.connectionstring ? "" : "This field is required."
        if ('username' in fieldValues)
            temp.username = fieldValues.username ? "" : "This field is required."
        if ('password' in fieldValues)
            temp.password = fieldValues.password ? "" : "This field is required."
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
                <Grid item lg={12} md={12} sm={12} xs={12}>
                    <Controls.Input
                        label="Connection String"
                        name="connectionstring"
                        value={values.connectionstring}
                        onChange={handleInputChange}
                        error={errors.connectionstring}
                    />
                    <Controls.Input
                        label="Username"
                        name="username"
                        value={values.username}
                        onChange={handleInputChange}
                        error={errors.username}
                    />
                    <Controls.Input
                        label="Password"
                        name="password"
                        value={values.password}
                        error={errors.password}
                        onChange={handleInputChange}
                    />
                </Grid>
                <Grid item lg={6} md={6} sm={12} xs={12}>
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

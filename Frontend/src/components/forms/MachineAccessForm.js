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
            background: "#1888ff"
          }
    }
}))

/**
 * Initial values
 */
const initialFValues = {
    username: '',
    password: '',
    description: ''
}

/**
 * Receives from parent component the add Or Edit function in order to submit
 * recordForEdit is used to get the record and then load its values on the form to perform
 * edit actions. 
 * add defines if this component will be used for an Add action or an Edit action
 * @param {*} props 
 * @returns 
 */
export default function MachineAccessForm(props) {
    const { addOrEdit, recordForEdit, add } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
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

    /**
     * If add then this form is used to create an machine access object
     */
    if(add) 
    {
        return (
            <Form onSubmit={handleSubmit}>
                <Grid container spacing={1}>
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                        <Controls.Input
                            name="username"
                            label="Username"
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
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                    <Controls.Input
                            label="Description"
                            name="description"
                            value={values.description}
                            onChange={handleInputChange}
                        />
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
    else
    {
        return (
            <Form onSubmit={handleSubmit}>
                <Grid container>
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                        <Controls.Input
                            label="Password"
                            name="password"
                            value={values.password}
                            error={errors.password}
                            onChange={handleInputChange}
                        />
                    </Grid>
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                    <Controls.Input
                            label="Description"
                            name="description"
                            value={values.description}
                            onChange={handleInputChange}
                        />
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
}

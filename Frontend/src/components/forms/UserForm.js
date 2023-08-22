import React, { useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const initialFValues = {
    username: '',
    role: 'USER'
}

const roles = [
    { id: 'ADMIN', title: 'ADMIN' },
    { id: 'USER', title: 'USER' },
    { id: 'VISITOR', title: 'VISITOR'}
]


const useStyles = makeStyles(({
    newButton: {
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

export default function UserForm(props) {
    const { addOrEdit, recordForEdit, add } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('username' in fieldValues)
            temp.username = fieldValues.username ? "" : "This field is required."
        if('role' in fieldValues)
            temp.role = fieldValues.role ? "" : "This field is required."
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

    if(add)
    {
        return (
            <Form onSubmit={handleSubmit}>
                <Grid item lg={12} md={12} sm={12} xs={12} container>
                        <Controls.Input
                            label="Username"
                            name="username"
                            value={values.username}
                            onChange={handleInputChange}
                        />
                        <Controls.Select
                            name="role"
                            label="User Role"
                            value={values.role}
                            onChange={handleInputChange}
                            options={roles}
                            error={errors.role}
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
            </Form>
        )
    }
    else
    {
        return (
            <Form onSubmit={handleSubmit}>
                <Grid item lg={12} md={12} sm={12} xs={12} container>
                        <Controls.Select
                            name="role"
                            label="User Role"
                            value={values.role}
                            onChange={handleInputChange}
                            options={roles}
                            error={errors.role}
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
            </Form>
        )
    }   
}

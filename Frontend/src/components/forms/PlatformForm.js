import React, { useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const initialFValues = {
    name: '',
    url: '',
    username: '',
    password: '',
    description: ''
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

export default function PlatformForm(props) {
    const { addOrEdit, recordForEdit, showCredentials, add, updateCredentials } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('name' in fieldValues)
            temp.name = fieldValues.name ? "" : "This field is required."
        if ('url' in fieldValues)
            temp.url = fieldValues.url ? "" : "This field is required."
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

    const handleSubmitCredentials = e => {
        e.preventDefault()
        if(validate()){
            updateCredentials(values, resetForm)
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
                <Grid container>
                    <Grid item lg={6} md={6} sm={12} xs={12}>
                        <Controls.Input
                            name="name"
                            label="Name"
                            value={values.name}
                            onChange={handleInputChange}
                            error={errors.name}
                        />
                        <Controls.Input
                            label="URL"
                            name="url"
                            value={values.url}
                            onChange={handleInputChange}
                            error={errors.url}
                        />
                        <Controls.Input
                            label="Description"
                            name="description"
                            value={values.description}
                            onChange={handleInputChange}
                        />
                    </Grid>
                    <Grid item lg={6} md={6} sm={12} xs={12}>

                         <Controls.Input
                            label="Username"
                            name="username"
                            value={values.username}
                            onChange={handleInputChange}
                        />
                        <Controls.Input
                            label="Password"
                            name="password"
                            value={values.password}
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
    else if(showCredentials && !add)
    {
        return (
            <Form onSubmit={handleSubmit}>
                <Grid container lg={12} md={12} sm={12} xs={12}>
                        <Controls.Input
                            label="Username"
                            name="username"
                            value={values.username}
                            onChange={handleInputChange}
                        />
                        <Controls.Input
                            label="Password"
                            name="password"
                            value={values.password}
                            onChange={handleInputChange}
                        />
                    </Grid>
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                        <div>
                            <Controls.Button
                                className={classes.newButton}
                                type="submit"
                                text="Update" 
                                onClick={handleSubmitCredentials}/>
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
                <Grid container>
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                        <Controls.Input
                            label="Platform URL"
                            name="url"
                            value={values.url}
                            onChange={handleInputChange}
                            error={errors.url}
                        />
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
}

import React from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const initialFValues = {
    name: ''
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


export default function PlatformsAvailableForm(props) {
    const { addOrEdit, platforms } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('url' in fieldValues)
            temp.name = fieldValues.name ? "" : "This field is required."
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
                        name="name"
                        label="Unused Available Platforms"
                        multiple={false}
                        value={values.name}
                        onChange={handleInputChange}
                        options={platforms}
                        error={errors.name}
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

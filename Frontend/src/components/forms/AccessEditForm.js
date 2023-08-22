import React, { useEffect } from 'react'
import { Grid } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { useForm, Form } from './useForm';
import { makeStyles } from '@material-ui/core';

/**
 * Set initial values
 */
const initialFValues = {
    description: ''
}

/**
 * Styles for button
 */
const useStyles = makeStyles(({
    newButton: {
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    },
    buttons: {
        alignContent: 'center',
        textAlign: 'center'
    }
}))

/**
 * Access Form
 * @param props 
 */
export default function AccessEditForm(props) {

    // receives props from Access Page
    const { addOrEdit, recordForEdit } = props

    //access to styles defined above
    const classes = useStyles()

    // Validation of required parameters
    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('description' in fieldValues)
            temp.description = fieldValues.description ? "" : "This field is required."
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

    /**
     * If every required parameters are check, send to 
     * AccessPage information via addoredit function
     * @param {Event} e 
     */
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
            <Grid >
                <Grid item lg={12} md={12} sm={12} xs={12}>
                  <Controls.Input 
                    name="description"
                    label="Description"
                    value={values.description}
                    onChange={handleInputChange}
                    error={errors.description}
                  />
                </Grid>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                    <div >
                        <br/>
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

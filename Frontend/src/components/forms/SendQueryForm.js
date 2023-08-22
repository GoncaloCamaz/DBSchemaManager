import React, { useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import { useForm, Form } from './useForm';

const initialFValues = {
    query: ''
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

export default function SendQueryForm(props) {
    const { addOrEdit, recordForEdit, returnToSchemas } = props
    
    const classes = useStyles()
    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('query' in fieldValues)
            temp.query = fieldValues.query ? "" : "This field is required."
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

    const handleReturnToSchemas = e =>
    {
        e.preventDefault()
        returnToSchemas();
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
            <Grid container spacing={6}>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                     <Controls.Input
                        label="Query"
                        name="query"
                        multiline={true}
                        rowsMax="20"
                        value={values.query}
                        error={errors.query}
                        onChange={handleInputChange}
                    />
                </Grid>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                    <div>
                        <Controls.Button
                            className={classes.newButton}
                            type="submit"
                            text="Send Query" 
                            onClick={handleSubmit}
                        />
                        <Controls.Button
                            text="Reset"
                            color="default"
                            onClick={resetForm} 
                        />
                         <Controls.Button
                            text="Return to Schemas"
                            color="default"
                            onClick={handleReturnToSchemas} 
                        />
                    </div>
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

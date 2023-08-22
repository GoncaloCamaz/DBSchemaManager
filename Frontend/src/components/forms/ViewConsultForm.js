import React, { useEffect, useState } from 'react'
import { Grid } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { format } from 'sql-formatter';
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
    script: ''
    }


export default function ViewConsultForm(props) {
    const classes = useStyles()
    const [editorEnabled, setEditorEnabled] = useState({enabled: true})
    const { addOrEdit, recordForEdit } = props

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
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
            addOrEdit(values, resetForm)
        }
    }

    const handleDisableSQLSyntaxSubmit = e => {
        e.preventDefault()
        setEditorEnabled(!editorEnabled)
    }

    useEffect(() => {
        if (recordForEdit != null)
            setValues({
                ...recordForEdit
            })
        else 
            setValues({
                ...initialFValues
            })
    // eslint-disable-next-line
    }, [recordForEdit])

    if(editorEnabled)
    {
        return (
            <Form onSubmit={handleSubmit}>
                <Grid container spacing={10}>
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                            <Controls.Input
                                label="View Code"
                                name="script"
                                fullWidth={true}
                                multiline={true}
                                rowsMax="200"
                                value={format(values.script)}
                                onChange={handleInputChange}
                            />
                    </Grid>   
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                    <Controls.Button
                            className={classes.newButton}
                            title={"Send Script to Create View Page"}
                            type="submit"
                            text="Send to Editor" 
                            onClick={handleSubmit}
                    /> 
                    <Controls.Button
                            className={classes.newButton}
                            title={"Disabling the Formatter Will Help While Editing"}
                            type="submit"
                            text="Disable SQL Syntax Formatter" 
                            onClick={handleDisableSQLSyntaxSubmit}
                    /> 
                    </Grid> 
                    <Grid item xs={3}>
                    </Grid> 
                    <Grid item xs={3}>
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
    else
    {
        return (
            <Form onSubmit={handleSubmit}>
                <Grid container spacing={10}>
                    <Grid item xs={12}>
                            <Controls.Input
                                label="View Code"
                                name="script"
                                fullWidth={true}
                                multiline={true}
                                rowsMax="200"
                                value={values.script}
                                onChange={handleInputChange}
                            />
                    </Grid>   
                    <Grid item xs={12}>
                    <Controls.Button
                            className={classes.newButton}
                            title={"Send Script to Create View Page"}
                            type="submit"
                            text="Send to Editor" 
                            onClick={handleSubmit}
                    /> 
                    <Controls.Button
                            className={classes.newButton}
                            title={"Enable Formatter to Help Reading the Script"}
                            type="submit"
                            text="Enable SQL Syntax Formatter" 
                            onClick={handleDisableSQLSyntaxSubmit}
                    /> 
                    </Grid> 
                    <Grid item xs={3}>
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
}
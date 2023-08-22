import React, { useEffect } from 'react'
import { Grid } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { format } from 'sql-formatter';
import { useForm, Form } from './useForm';

const initialFValues = {
    script: ''
}

export default function ProcedureConsultForm(props) {
    const { recordForEdit } = props

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
    } = useForm(initialFValues, true, validate);

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

    return (
            <Form>
                <Grid container spacing={10}>
                    <Grid item lg={12} md={12} sm={12} xs={12}>
                        <Controls.Input
                            label="Procedure Code"
                            name="script"
                            multiline={true}
                            rowsMax="25"
                            value={format(values.script)}
                            onChange={handleInputChange}
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
                    <Grid item xs={3}>
                    </Grid>
                    <Grid item xs={3}>
                    </Grid>        
                </Grid>
            </Form>
     )
}
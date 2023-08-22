import React, { useEffect } from 'react'
import { Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { useForm, Form } from './useForm';
import { makeStyles } from '@material-ui/core';

/**
 * Machine type to define if it is virtual or physical
 */
const machineType = [
    { id: 'Virtual', title: 'Virtual' },
    { id: 'Physical', title: 'Physical' }
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

const initialFValues = {
    name: '',
    operativesystem: '',
    serviceip: '',
    managementip: '',
    description: '',
    observations: '',
    machinetype: 'Physical',
}

/**
 * Machine form
 * @param {*} props add or edit function, recordForEdit to use values if it is an edit form and the add
 * to define if its an edit or an add form
 * @returns Form
 */
export default function MachineForm(props) {
    const { addOrEdit, recordForEdit, add } = props
    const classes = useStyles()

    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('name' in fieldValues)
            temp.name = fieldValues.name ? "" : "This field is required."
        if ('machinetype' in fieldValues)
            temp.machinetype = fieldValues.machinetype ? "" : "This field is required."
        if ('serviceip' in fieldValues)
            temp.serviceip = fieldValues.serviceip ? "" : "This field is required."
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
                <Grid container>
                    <Grid item lg={6} md={6} sm={12} xs={12}>
                        <Controls.RadioGroup
                            name="machinetype"
                            label="Type"
                            value={values.machinetype}
                            onChange={handleInputChange}
                            items={machineType}
                            error={errors.machinetype}
                        />
                        <Controls.Input
                            name="name"
                            label="Name"
                            value={values.name}
                            onChange={handleInputChange}
                            error={errors.name}
                        />
                        <Controls.Input
                            label="Operative System"
                            name="operativesystem"
                            value={values.operativesystem}
                            onChange={handleInputChange}
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
                            label="Service IP"
                            name="serviceip"
                            value={values.serviceip}
                            error={errors.serviceip}
                            onChange={handleInputChange}
                        />
                        <Controls.Input
                            label="Management IP"
                            name="managementip"
                            value={values.managementip}
                            onChange={handleInputChange}
                        />
                        <Controls.Input
                            label="Observations"
                            name="observations"
                            value={values.observations}
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
                    <Grid item lg={6} md={6} sm={12} xs={12}>
                        <Controls.RadioGroup
                            name="machinetype"
                            label="Type"
                            value={values.machinetype}
                            onChange={handleInputChange}
                            items={machineType}
                            error={errors.machinetype}
                        />
                        <Controls.Input
                            label="Operative System"
                            name="operativesystem"
                            value={values.operativesystem}
                            onChange={handleInputChange}
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
                            label="Service IP"
                            name="serviceip"
                            value={values.serviceip}
                            error={errors.serviceip}
                            onChange={handleInputChange}
                        />
                        <Controls.Input
                            label="Management IP"
                            name="managementip"
                            value={values.managementip}
                            onChange={handleInputChange}
                        />
                        <Controls.Input
                            label="Observations"
                            name="observations"
                            value={values.observations}
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

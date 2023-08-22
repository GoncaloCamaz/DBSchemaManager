import React, { useEffect } from 'react'
import { Grid } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { useForm, Form } from './useForm';
import { makeStyles } from '@material-ui/core';

/**
 * Set initial values
 */
const initialFValues = {
    schema: '',
    dbobject: '',
    user: '',
    privilege: 'SELECT', // value by default
    permission: 'grant', // value by default
}

/**
 * Type of grant
 */
const grantItems = [
    { id: 'grant', title: 'Grant' },
    { id: 'revoke', title: 'Revoke' }
]

/**
 * Privileges types
 */
const privileges = [
    { id: 'ALL', title: 'ALL' },
    { id: 'CREATE', title: 'CREATE' },
    { id: 'EXECUTE', title: 'EXECUTE' },
    { id: 'SELECT', title: 'SELECT' } 
]

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
export default function AccessForm(props) {

    // receives props from Access Page
    const { addOrEdit, recordForEdit, handleSchemaSelect } = props

    //access to styles defined above
    const classes = useStyles()

    // Validation of required parameters
    const validate = (fieldValues = values) => {
        let temp = { ...errors }
        if ('schema' in fieldValues)
            temp.schema = fieldValues.schema ? "" : "This field is required."
        if ('dbobject' in fieldValues)
            temp.dbobject = fieldValues.dbobject ? "" : "This field is required."
        if ('user' in fieldValues)
            temp.user = fieldValues.user ? "" : "This field is required."
        if ('permission' in fieldValues)
            temp.permission = fieldValues.permission ? "" : "This field is required."
        if ('privilege' in fieldValues)
            temp.privilege = fieldValues.privilege ? "" : "This field is required."
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

    /**
     * Asks AccessPage to load information about schema
     * Objects and Users
     * @param {event} e 
     */
    const handleSchemaCHange = e => {
        e.preventDefault()
        handleInputChange(e)
        handleSchemaSelect(e.target.value)
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
                    <Controls.RadioGroup
                        name="permission"
                        label="Permission Type"
                        value={values.permission}
                        onChange={handleInputChange}
                        checked={values.permission}
                        items={grantItems}
                        error={errors.permission}
                    />
                    <Controls.Select
                        name="schema"
                        label="Schema"
                        value={values.schema}
                        onChange={handleSchemaCHange}
                        options={props.schemas}
                        error={errors.schema}
                    />
                     <Controls.Select
                        name="dbobject"
                        label="Object"
                        value={values.dbobject}
                        onChange={handleInputChange}
                        options={props.dbobjects}
                        error={errors.dbobject}
                    />
                    <Controls.Select
                        name="user"
                        label="User"
                        value={values.user}
                        onChange={handleInputChange}
                        options={props.users}
                        error={errors.user}
                    />
                    <Controls.Select
                        name="privilege"
                        label="Privilege"
                        value={values.privilege}
                        onChange={handleInputChange}
                        options={privileges}
                        error={errors.privilege}
                    />
                </Grid>
                <Grid item lg={6} md={6} sm={12} xs={12}>
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

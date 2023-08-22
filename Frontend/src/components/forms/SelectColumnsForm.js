import React, { useState } from 'react'
import { FormControlLabel, FormGroup, Grid, } from '@material-ui/core';
import Controls from "../../components/controls/Controls";
import { makeStyles } from '@material-ui/core';
import Checkbox from '../controls/Checkbox';

const useStyles = makeStyles(({
    newButton: {
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

export default function SelectColumnsForm(props) {
    const headers = props.headers
    var content = []
    headers.forEach(item => content.push({name: item.id, checked: false}))
    const classes = useStyles()
    const [columnsSelected, setColumnsSelected] = useState(content)    
    
    const handleSubmit = _event => {
        const selected = columnsSelected.filter(item => item.checked === true)
        props.selectedHeaders(selected)
    }

    const handleChange = (event) => {
        var content = columnsSelected
        
        for(const elm in content)
        {
            if(content[elm].name === event.target.name)
            {
                content[elm].checked = !content[elm].checked
            }
        }
        setColumnsSelected(content)
    }

    return (
            <Grid container>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                    <FormGroup>
                        {columnsSelected.map((item, index) => ( 
                            <FormControlLabel key={index}
                                control={<Checkbox checked={item.checked} onChange={handleChange} name={item.name}/>}
                                label={item.name}
                            />
                            ))
                        }
                    </FormGroup>
                    <div>
                        <Controls.Button
                            className={classes.newButton}
                            type="submit"
                            text="Download" 
                            onClick={handleSubmit}/>
                    </div>
                </Grid>
            </Grid>
    )
}

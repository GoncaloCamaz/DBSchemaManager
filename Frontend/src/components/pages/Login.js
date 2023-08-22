import React, { useContext, useState } from 'react';
import './Login.css'
import { Context } from '../../context/AuthContext';
import {Button, Form, FormGroup, Input } from 'reactstrap'

export default function Login() {
  const { handleLogin } = useContext(Context);
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")

  const handleClick = () => {
    handleLogin(username,password) 
  }

  return (
    <div>
      <div className='login-box'>
        <h2>DBSchema Manager</h2>
        <div className='form-container'>
          <Form className='user-box'>
            <FormGroup>
              <Input type='text' placeholder='Username' onChange={(event) => {setUsername({username:event.target.value})}}/>
            </FormGroup>
            <FormGroup>
              <Input autoComplete="" type='password' placeholder='Password' onChange={(event) => {setPassword({password:event.target.value})}}/>
            </FormGroup>
              <Button className='btn_login' onClick={handleClick}> Login</Button>
          </Form>
        </div>
      </div>
    </div>
  );
}
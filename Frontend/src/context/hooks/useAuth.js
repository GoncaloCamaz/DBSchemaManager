import { useState, useEffect } from 'react';
import history from '../../history';
import axios from 'axios'
import { backendURL } from '../../constants';

export default function useAuth() {
  const [authenticated, setAuthenticated] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      setAuthenticated(true);
    }
    setLoading(false);
  }, []);
  
  function handleLogin(paramusername, parampassword) {
    const URL = backendURL+'/dbschemamanager/login'
    const toReturn = {
      userrole: '',
      authenticated: false
    }

    setLoading(true)
    axios.post(URL, {
      username: paramusername.username,
      password: parampassword.password || ''
    })
    .then((response) => {
      if(response.status === 200)
      {
        const authenticatedInfo = {
          userrole: response.data.role,
          authenticated: true
        }
        localStorage.setItem('token', response.data.token)
        localStorage.setItem('username', paramusername.username)
        localStorage.setItem('userrole',response.data.role)
        setLoading(false)
        setAuthenticated(true);
        history.push('/home');
        return authenticatedInfo;
      }
    }).catch(error => {
      window.alert("Bad credentials inserted!")
      setLoading(false)
      setAuthenticated(false);
      return toReturn;
    });
    setLoading(false)
    return toReturn
  }

  function handleLogout() {
    setAuthenticated(false);
    localStorage.removeItem('token');
    localStorage.removeItem('userrole');
    localStorage.removeItem('username')
    history.push('/');
  }
  
  return { authenticated, loading, handleLogin, handleLogout };
}
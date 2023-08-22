import React from 'react';
import { Router } from 'react-router-dom';
import Routes from './routes';
import history from './history';

import { AuthProvider } from './context/AuthContext';

/**
 * 
 * @returns Authentication and Routes
 */

function App() {
  return (
    <AuthProvider>
      <Router history={history}>
        <Routes />
      </Router>
    </AuthProvider>
  );
}

export default App;
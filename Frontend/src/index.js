import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';

/**
 * This is the starter of everything. The App is rendered within
 * DOM on element root on the index.html.
 * 
 * This application is structured inside App with authentication 
 * components and routes that map an url path to render a specific component
 * Each component may contain other sub-components that receive information from
 * the parent conpoment such as machines name in order to fetch objects regarding that 
 * machine name.
 */

ReactDOM.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  document.getElementById('root')
);

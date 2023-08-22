import React, { useContext } from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import { Context } from './context/AuthContext';
import AccountPage from './components/pages/AccountPage'
import AccessHistoryPage from './components/pages/AccessHistoryPage'
import AccessPage from './components/pages/AccessPage'
import ColumnsPage from './components/pages/ColumnsPage'
import CreateAccessPage from './components/pages/CreateAccessPage'
import CreateViewPage from './components/pages/CreateViewPage'
import DatabasesPage from './components/pages/DatabasesPage'
import HomePage from './components/pages/HomePage'
import Login from './components/pages/Login'
import ManagementPage from './components/pages/ManagementPage'
import ObjectsPage from './components/pages/ObjectsPage'
import ProceduresHistoryPage from './components/pages/ProceduresHistoryPage'
import SchemaPage from './components/pages/SchemaPage'
import SchemaQueryPage from './components/pages/SchemaQueryPage'
import UserAccountPage from './components/pages/UserAccountPage'
import ViewsPage from './components/pages/ViewsPage'
import ViewHistoryPage from './components/pages/ViewHistoryPage'
import './index.css'

function CustomRoute({ isPrivate, ...rest }) {
  const { loading, authenticated } = useContext(Context);

  if (loading) {
    return (
      <div className='page_wrapper'>
          <div className='logo_wheel'></div>
          <h4 className='blinkText'>Loading..</h4>
      </div>
    )
  }

  /**
   * Redirect to initial login page if not able to authenticate
   */
  if (isPrivate && !authenticated) {
    return <Redirect to="/" />
  }

  return <Route {...rest} />;
}

/**
 * 
 * @returns Route definitions and its components
 */
export default function Routes() {
  return (
        <Switch>
          <CustomRoute exact path="/" component={Login} />
          <CustomRoute isPrivate exact path='/home' component={HomePage}/>
          <CustomRoute isPrivate exact path='/databases' component={DatabasesPage}/>
          <CustomRoute isPrivate exact path='/databases/schemas' component={SchemaPage}/>
          <CustomRoute isPrivate exact path='/databases/schemas/query' component={SchemaQueryPage}/>
          <CustomRoute isPrivate exact path='/databases/objects' component={ObjectsPage}/>
          <CustomRoute isPrivate exact path='/databases/columns' component={ColumnsPage}/>
          <CustomRoute isPrivate exact path='/management' component={ManagementPage}/>
          <CustomRoute isPrivate exact path='/management/accesses' component={AccessPage}/>
          <CustomRoute isPrivate exact path='/management/accesses/create' component={CreateAccessPage}/>
          <CustomRoute isPrivate exact path='/management/accesses/history' component={AccessHistoryPage}/>
          <CustomRoute isPrivate exact path='/management/views' component={ViewsPage}/>
          <CustomRoute isPrivate exact path='/management/views/create' component={CreateViewPage}/>
          <CustomRoute isPrivate exact path='/management/procedures' component={ProceduresHistoryPage}/>
          <CustomRoute isPrivate exact path='/management/views/history' component={ViewHistoryPage}/>
          <CustomRoute isPrivate exact path='/accounts' component={AccountPage}/>
          <CustomRoute isPrivate exact path='/accounts/users' component={UserAccountPage}/>
      </Switch>
  );
}
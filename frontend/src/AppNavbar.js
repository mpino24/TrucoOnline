import React, { useState, useEffect } from 'react';
import { NavLink, NavItem } from 'reactstrap';
import { Link, useNavigate } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from 'jwt-decode';
import LeavingGameModal from './components/LeavingGameModal';

function AppNavbar() {
  const [roles, setRoles] = useState([]);
  const jwt = tokenService.getLocalAccessToken();
  const navigate = useNavigate();
  const [leavingModal, setLeavingModal] = useState(false);
  const logoChico = "https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/resources/images/logoChico.png"
  useEffect(() => {
    if (jwt) {
      setRoles(jwt_decode(jwt).authorities);
    }
  }, [jwt]);

  let adminLinks = <></>;
  let userLinks = <></>;

  roles.forEach((role) => {
    if (role === 'ADMIN') {
      adminLinks = (
        <>
          <NavItem>
            <NavLink style={{ color: 'white' }} tag={Link} to="/users">
              Users
            </NavLink>
          </NavItem>
        </>
      );
    }
  });

  function handleClick() {
    if (jwt) {
      fetch('https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/partidajugador/partidaJugadorActual', {
        method: 'GET',
        headers: {
          Authorization: `Bearer ${jwt}`,
        },
      })
        .then((response) => {
          if (response.status === 202) {
            return null;
          }
          return response.json();
        })
        .then((data) => {
          if (data !== null && data.game && data.game.estado !== 'FINALIZADA') {
            setLeavingModal(true);
          } else {
            navigate('/home');
          }
        })
        .catch((message) => alert(message));
    } else {
      navigate('/');
    }
  }

  return (
    <div
      expand="md"
      dark
      style={{
        float: 'left',
        overflow: 'hidden',
        background: 'transparent',   // <-- Transparent instead of white
        top: 0,
        position:"fixed",
        left: 0,
        width: '20%',
        zIndex: 9999,                // <-- Ensures it stays above other elements
      }}
    >
      <img
        alt="logo"
        src={logoChico}
        onClick={handleClick}
        style={{
          height: 90,
          width: 90,
          borderRadius: 500,
          margin: 10,
          cursor: 'pointer',
        }}
      />
      <LeavingGameModal
        modalIsOpen={leavingModal}
        setIsOpen={setLeavingModal}
      />
      {/* 
        If you need your adminLinks or userLinks, place them here, 
        for example in a 'nav' or other container.
        
        {adminLinks}
        {userLinks}
      */}
    </div>
  );
}

export default AppNavbar;

import React, { useState, useEffect, useCallback } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link, Router } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import logoChico from './/static/images/logoChico.png';
//import fotoPerfil from './/static/images/fotoPerfil.jpg';

function AppNavbar() {
    const [roles, setRoles] = useState([]);
    const [username, setUsername] = useState("");
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);
    const [showAccountMenu, setShowAccountMenu] = useState(false);
    const [photoUrl,setPhotoUrl] = useState('/fotoPerfil.jpg')
    


    const toggleNavbar = () => setCollapsed(!collapsed);

    const toggleAccountMenu = useCallback(() => {
        setShowAccountMenu((current) => !current);
    }, []);

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
            setUsername(jwt_decode(jwt).sub);
        }
    }, [jwt])

    let adminLinks = <></>;
    let userLinks = <></>;
    roles.forEach((role) => {
        if (role === "ADMIN") {
            adminLinks = (
                <>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/users">Users</NavLink>
                    </NavItem>
                </>
            )
        }
    })


    return (
        <div>
            <Navbar expand="md" dark >
                <NavbarBrand href="/">
                    <img alt="logo" src={logoChico} style={{ height: 80, width: 80, borderRadius: 500 }} />
                </NavbarBrand>
                <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                    <div style={{ display: 'flex', alignItems: 'center', cursor:'pointer' }} onClick={toggleAccountMenu}>
                        <p style={{ color: "white", marginRight: 20, fontSize: 20 }} >{username}</p>
                        <img style={{ height: 60, width: 60, borderRadius: 500 }} src={photoUrl} alt='Foto de perfil del usuario'></img>
                    </div>
                </Nav>
            </Navbar>
            {showAccountMenu &&
                <div style={{ backgroundColor: 'black', borderRadius: 5, height: 70, width: 200, float: 'right', marginRight: 5 }}>
                    <NavItem className="d-flex">
                        <NavLink style={{ color: "white", marginTop: 8, marginLeft: 5 }} id="perfil" tag={Link} to="/profile/">Mi Perfil</NavLink>
                    </NavItem>

                    <NavItem className="d-flex">
                        <NavLink style={{ color: "red", marginBottom: 2, marginLeft: 5 }} id="logout" tag={Link} to="/logout">Cerrar Sesión</NavLink>
                    </NavItem>
                </div>
            }
        </div>

    );
}

export default AppNavbar;
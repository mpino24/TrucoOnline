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
    const [photoUrl,setPhotoUrl] = useState('/fotoPerfil.jpg');
    const [escapeLink,setEscapeLink] = useState("/");
    


    const toggleNavbar = () => setCollapsed(!collapsed);

    const toggleAccountMenu = useCallback(() => {
        setShowAccountMenu((current) => !current);
    }, []);

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
            setUsername(jwt_decode(jwt).sub);
            setEscapeLink("/home");
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
            <Navbar expand="md" dark style={{float:'left'}} >
                <NavbarBrand href={escapeLink}>
                    <img alt="logo" src={logoChico} style={{ height: 80, width: 80, borderRadius: 500 }} />
                </NavbarBrand>
             </Navbar>
        </div>

    );
}

export default AppNavbar;
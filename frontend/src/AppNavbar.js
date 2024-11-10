import React, { useState, useEffect, useCallback } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link, Router, useNavigate } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import logoChico from './/static/images/logoChico.png';

function AppNavbar() {
    const [roles, setRoles] = useState([]);
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);
    const navigate  = useNavigate();
    



    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
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

    function handleClick(){
        if(jwt){
            fetch(
                "/api/v1/partidajugador/connectedTo/"+tokenService.getUser().id,
                {
                    method: "GET"
                }
            )
                .then((response) => response.text())
                .then((data) => {
                    if (data>0) {
                        alert(data);
                    }
                    else {
                        navigate("/home");
    
                    }
                })
                .catch((message) => alert(message));
        }else{
            navigate("/"); 
        }
        
    }

    return (
        <div expand="md" dark style={{float:'left'}}>
            <img alt="logo" src={logoChico} onClick={()=> handleClick()} style={{ height: 90, width: 90, borderRadius: 500, margin:10, cursor:'pointer' }} />
        </div>

    );
}

export default AppNavbar;
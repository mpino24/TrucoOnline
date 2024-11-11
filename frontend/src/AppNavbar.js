import React, { useState, useEffect, useCallback } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link, Router, useNavigate } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import logoChico from './/static/images/logoChico.png';
import Modal from 'react-modal';

function AppNavbar() {
    const [roles, setRoles] = useState([]);
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);
    const navigate = useNavigate();
    const [modalIsOpen, setIsOpen] = useState(false);

    const customModalStyles = {
        content: {
            top: '50%',
            left: '50%',
            right: 'auto',
            bottom: 'auto',
            marginRight: '-50%',
            transform: 'translate(-50%, -50%)',
            width: '400px',
            padding: '20px',
            borderRadius: '10px',
            boxShadow: '0px 4px 12px rgba(0, 0, 0, 0.15)',
            textAlign: 'center',
            border: 'none'
        },
        overlay: {
            backgroundColor: 'rgba(0, 0, 0, 0.6)'
        }
    };



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

    function handleClick() {
        if (jwt) {
            fetch(
                "/api/v1/partidajugador/connectedTo/" + tokenService.getUser().id,
                {
                    method: "GET"
                }
            )
                .then((response) => response.text())
                .then((data) => {
                    if (data > 0) {
                        setIsOpen(true)
                    }
                    else {
                        navigate("/home");

                    }
                })
                .catch((message) => alert(message));
        } else {
            navigate("/");
        }

    }

    function closeModal() {
        setIsOpen(false);
    }

    function leaveGame() {
        fetch(
            "/api/v1/partidajugador?userId=" + tokenService.getUser().id,
            {
                method: "DELETE"
            }
        )
            .then((response) => response.text())
            .then((data) => {
                setIsOpen(false);
                navigate("/home");
            })
            .catch((message) => alert(message));

    }

    return (
        <div expand="md" dark style={{ float: 'left' }}>
            <img alt="logo" src={logoChico} onClick={() => handleClick()} style={{ height: 90, width: 90, borderRadius: 500, margin: 10, cursor: 'pointer' }} />
            <Modal
                isOpen={modalIsOpen}
                onRequestClose={closeModal}
                style={customModalStyles}
                ariaHideApp={false}
            >
                <h2 style={{ marginBottom: '20px', color: '#333' }}>¿Quieres abandonar la partida?</h2>
                <img src="https://c.tenor.com/vkvU9Fi4uOsAAAAC/tenor.gif" alt="Mono GIF" style={{ width: '100%', height: 'auto', marginBottom: '20px' }} />
                <div style={{ display: 'flex', justifyContent: 'space-around' }}>
                    <button
                        onClick={leaveGame}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#ff4d4d',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontSize: '16px',
                            transition: 'background-color 0.3s ease',
                            marginRight: '10px'
                        }}
                        onMouseEnter={(e) => (e.target.style.backgroundColor = '#ff3333')}
                        onMouseLeave={(e) => (e.target.style.backgroundColor = '#ff4d4d')}
                    >
                        Sí, abandonar partida
                    </button>
                    <button
                        onClick={closeModal}
                        style={{
                            padding: '10px 20px',
                            backgroundColor: '#4CAF50',
                            color: '#fff',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontSize: '16px',
                            transition: 'background-color 0.3s ease'
                        }}
                        onMouseEnter={(e) => (e.target.style.backgroundColor = '#45a049')}
                        onMouseLeave={(e) => (e.target.style.backgroundColor = '#4CAF50')}
                    >
                        No, me quedo
                    </button>
                </div>
            </Modal>
        </div>
    );
}

export default AppNavbar;
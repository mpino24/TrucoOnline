import React, { useState, useEffect } from 'react';
import { NavLink, NavItem} from 'reactstrap';
import { Link, useNavigate } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import logoChico from './/static/images/logoChico.png';
import LeavingGameModal from './components/LeavingGameModal';

function AppNavbar() {
    const [roles, setRoles] = useState([]);
    const jwt = tokenService.getLocalAccessToken();
    const navigate = useNavigate();
    const [leavingModal,setLeavingModal] = useState(false);




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
                        setLeavingModal(true)
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


    return (
        <div expand="md" dark style={{ float: 'left', zIndex:21 }}>
            <img alt="logo" src={logoChico} onClick={() => handleClick()} style={{ height: 90, width: 90, borderRadius: 500, margin: 10, cursor: 'pointer' }} />
            <LeavingGameModal
            modalIsOpen={leavingModal}
            setIsOpen={setLeavingModal}/>
        </div>
    );
}

export default AppNavbar;
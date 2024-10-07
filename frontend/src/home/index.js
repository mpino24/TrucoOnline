import React, { useState, useCallback } from 'react';
import '../App.css';
import '../static/css/home/home.css';
import '../static/css/home/homeButton.css';
import { Form, Label, Input } from "reactstrap";
import { FaUserFriends } from "react-icons/fa";
import { RiArrowRightDoubleLine } from "react-icons/ri";
import { RiArrowLeftDoubleLine } from "react-icons/ri";
import { IoTrophy } from "react-icons/io5";
import {DropdownItem, DropdownMenu, DropdownToggle} from 'reactstrap';

export default function Home() {
    const backgrounds= [
        'url(/fondos/fondo0.jpg)',
        'url(/fondos/fondo1.jpg)',
        'url(/fondos/fondo2.jpg)',
        'url(/fondos/fondo3.jpg)',
        'url(/fondos/fondo4.jpg)',
        'url(/fondos/fondo5.jpg)',
        'url(/fondos/fondo6.jpg)',
        'url(/fondos/fondo7.jpg)',
        'url(/fondos/fondo8.jpg)',
        'url(/fondos/fondo9.jpg)',
    ];
    const [creationModal, setCreationModalView] = useState(false);

    const getRandomBackground = () => { //Generado por IA
        const randomIndex = Math.floor(Math.random() * backgrounds.length);
        return backgrounds[randomIndex];
    }
    const toggleCreationModal = useCallback(() => {
        setCreationModalView((current) => !current);
    }, []);
    
    return (

        <div>
          <div className="home-page-container" style={{background: getRandomBackground(), backgroundSize:'cover'}}>
            <div style={{width:'36%' }}>
                <div style={{cursor:'pointer'}}>
                <IoTrophy style={{ width: 80, height: 80, float: 'left',color:'white' }} />
                <RiArrowRightDoubleLine style={{ width: 80, height: 80, float: 'left',color:'white' }} />
                <div/>
            </div>
            </div>

            
          {creationModal &&  <div className="hero-div">
                <h1>¿Un truco?</h1>
                <button onPress={()=>toggleCreationModal()} className="home-button">Crear</button>
                <button className="home-button">Unirte</button>
            </div> }


             { !creationModal && <div className= 'cuadro-creacion'>
             <Form>
                <h3> Ajuste de la partida: </h3>
                        <div>
                            <Label>Numero de jugadores: 
                            </Label>
                            <DropdownItem>
                            <DropdownToggle> . </DropdownToggle>             
                            <DropdownMenu>
                                <DropdownItem >2</DropdownItem>
                                <DropdownItem >4</DropdownItem>
                                <DropdownItem >6</DropdownItem>
                            </DropdownMenu>
                            </DropdownItem>

                        </div>
                        
                        <Input type="checkbox" id="checkboxInput">
                            <Label for="checkboxInput" class="toggleSwitch"> Numero de puntos para ganar:
                            </Label>
                        </Input>
                        <div>
                            <Label>
                                Con Flor:
                            </Label>
                            </div>
                            <div>
                            <Label>
                                Partida privada:
                            </Label>
                            </div>
                            
                            <div className="botones">
                            <button onPress={()=>toggleCreationModal()} className="recuadro">Crear</button>
                            <button className="recuadro">Volver</button>
                            </div>
                    </Form>
            </div>}

            <div style={{ width:'36%' }}>
                <div style={{cursor:'pointer'}} >
                <FaUserFriends style={{ width: 80, height: 80, float: 'right',color:'white' }} />
                <RiArrowLeftDoubleLine style={{ width: 80, height: 80, float: 'right',color:'white' }} />
                </div>
            </div>

            <div style={{ backgroundColor: 'black', position: 'fixed', bottom: 0, width: '100%', height: 41 }}>
                <center style={{ color: 'white', marginTop: 5 }}>© MIDPIE</center>
            </div>
        </div> 
    </div>
    );

}


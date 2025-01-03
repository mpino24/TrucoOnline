import { useState, forwardRef } from 'react';

import { TbFlower } from "react-icons/tb";
import { TbFlowerOff } from "react-icons/tb";
import { Link } from 'react-router-dom';
import { ScrollView } from 'react-native-web';
import PartidaView from './PartidaView';
import JugadorView from './JugadorView';


const JugadorList = forwardRef((props, ref)  => {


    const jugadoresList = props.jugadores.map((player) => {
        return (
            <div onClick={()=> props.mostrarChat(player)}>
            <JugadorView
            jugador={player}
            isFriend={true}
            isSolicitud={true}
            />
            </div>
        )
    });
    return (

            <div style={{height: '100vh'}}>
                {jugadoresList}
                <div style={{height: '6vh'}}></div>
            </div>

        
    )
}
)
export default JugadorList;
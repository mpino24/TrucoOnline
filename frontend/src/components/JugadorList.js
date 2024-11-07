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
            
            <JugadorView
            jugador={player}
            isFriend={true}
            />
        )
    });
    return (
        <div>
            <p style={{overflowY:true}}>{jugadoresList}</p>
        </div>
        
    )
}
)
export default JugadorList;
import React, { forwardRef} from 'react';
import { Link } from "react-router-dom";
import {Button} from "reactstrap";


const FinishedModal = forwardRef((props, ref) => {

    const ganador= props.puntosEquipo1 === props.puntosMaximos?0:1;
    return (
        <div>
            <h2 className="player-heading"> Ganador: Equipo {ganador}</h2>
            <div className="botones">
            <Button className='recuadro' >
                <Link
                to={`/home`}
                style={{ textDecoration: "none" }}>Volver</Link>
                </Button>
            </div>
        </div>

    )
})

export default FinishedModal;
import React, { forwardRef } from 'react';
import Puntos0 from '../static/imagenesPuntos/Puntos0.png';
import Puntos1 from '../static/imagenesPuntos/Puntos1.png';
import Puntos2 from'../static/imagenesPuntos/Puntos2.png';
import Puntos3 from'../static/imagenesPuntos/Puntos3.png';
import Puntos4 from'../static/imagenesPuntos/Puntos4.png';
import Puntos5 from'../static/imagenesPuntos/Puntos5.png';
import Puntos6 from'../static/imagenesPuntos/Puntos6.png';
import Puntos7 from'../static/imagenesPuntos/Puntos7.png';
import Puntos8 from'../static/imagenesPuntos/Puntos8.png';
import Puntos9 from'../static/imagenesPuntos/Puntos9.png';
import Puntos10 from'../static/imagenesPuntos/Puntos10.png';
import Puntos11 from'../static/imagenesPuntos/Puntos11.png';
import Puntos12 from'../static/imagenesPuntos/Puntos12.png';
import Puntos13 from'../static/imagenesPuntos/Puntos13.png';
import Puntos14 from'../static/imagenesPuntos/Puntos14.png';
import Puntos15 from'../static/imagenesPuntos/Puntos15.png';
import Puntos16 from'../static/imagenesPuntos/Puntos16.png';
import Puntos17 from'../static/imagenesPuntos/Puntos17.png';
import Puntos18 from'../static/imagenesPuntos/Puntos18.png';
import Puntos19 from'../static/imagenesPuntos/Puntos19.png';
import Puntos20 from'../static/imagenesPuntos/Puntos20.png';
import Puntos21 from'../static/imagenesPuntos/Puntos21.png';
import Puntos22 from'../static/imagenesPuntos/Puntos22.png';
import Puntos23 from'../static/imagenesPuntos/Puntos23.png';
import Puntos24 from'../static/imagenesPuntos/Puntos24.png';
import Puntos25 from'../static/imagenesPuntos/Puntos25.png';
import Puntos26 from'../static/imagenesPuntos/Puntos26.png';
import Puntos27 from'../static/imagenesPuntos/Puntos27.png';
import Puntos28 from'../static/imagenesPuntos/Puntos28.png';
import Puntos29 from'../static/imagenesPuntos/Puntos29.png';
import Puntos30 from'../static/imagenesPuntos/Puntos30.png';



const imagenesPuntos = {
    0:Puntos0,
    1: Puntos1,
    2: Puntos2,
    3: Puntos3,
    4 : Puntos4,
    5:Puntos5,
    6: Puntos6,
    7: Puntos7,
    8: Puntos8,
    9: Puntos9,
    10: Puntos10,
    11: Puntos11,
    12: Puntos12,
    13: Puntos13,
    14:Puntos14,
    15: Puntos15,
    16: Puntos16,
    17: Puntos17,
    18: Puntos18,
    19: Puntos19,
    20: Puntos20,
    21: Puntos21,
    22: Puntos22,
    23: Puntos23,
    24: Puntos24,
    25: Puntos25,
    26: Puntos26,
    27: Puntos27,
    28: Puntos28,
    29: Puntos29,
    30: Puntos30
}

const PuntosComponente = forwardRef((props, ref) => {
    const imagenEquipo1 = imagenesPuntos[props.puntosEquipo1];
    const imagenEquipo2 = imagenesPuntos[props.puntosEquipo2];


    return (
        <div>
        <h6 className={props.estiloFotoPunto}> {props.posicion%2==0?
                <img
                    src={imagenEquipo1}
                    alt={`Puntos ${props.puntosEquipo1}`}
                    onError={(e) => (e.target.style.display = 'none')}
                />
                    :  
                <img
                    src={imagenEquipo2}
                    alt={`Puntos ${props.puntosEquipo2}`}
                    onError={(e) => (e.target.style.display = 'none')}
                    />}</h6>
                
            </div>
    )
})

export default PuntosComponente;
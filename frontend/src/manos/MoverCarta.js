import React, { useRef, useState } from 'react'; 


const MoverCarta = ({src}) => {

    const cartaAMover = useRef(src);
    const [positionCarta, setPositionCarta] = useState({ x: 0, y: 0 }); 

    const [isDragging, setIsDragging] = useState(false);

    const handleMouseDown = (e) => { 
        const carta = e.current;
        const shiftX = e.clientX - carta.getBoundingClientRect().left; 
        const shiftY = e.clientY - carta.getBoundingClientRect().top; 
        
        setIsDragging(true);

        const handleMouseMove = (e) => { 
            setPositionCarta({ x: e.clientX - shiftX, y: e.clientY - shiftY, });
        };

        const handleMouseUp = () => { 
            setIsDragging(false);
            document.removeEventListener('mousemove', handleMouseMove); 
            document.removeEventListener('mouseup', handleMouseUp); 
        };

        document.addEventListener('mousemove', handleMouseMove); 
        document.addEventListener('mouseup', handleMouseUp); 
    };
    
    return ( 
        <div>
            
                <img 
                    ref={cartaAMover}
                    src = {src}
                    alt = ''
                    className="draggable" 
                    style={{ position: 'absolute', left: `${positionCarta.x}px`, top: `${positionCarta.y}px` }} 
                    onMouseDown={handleMouseDown}/>
            
        </div> );
};

export default MoverCarta;












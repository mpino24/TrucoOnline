// TheaterTransition.js
import React, { useEffect, useState } from 'react';
import './TheaterTransition.css'; // We'll define styles here

export default function TheaterTransition({ children }) {
  // Whether curtains are open or closed
  const [curtainsOpen, setCurtainsOpen] = useState(false);

  useEffect(() => {
    // Wait ~300ms to ensure the <Game /> is mounted behind the curtains
    const timer = setTimeout(() => {
      // Then animate curtains open
      setCurtainsOpen(true);
    }, 300);

    return () => clearTimeout(timer);
  }, []);

  return (
    <div className="theater-container">
      {/* Render whatever you wrap inside TheaterTransition (e.g., <Game />) */}
      {children}

      {/* The absolute overlay that covers the entire screen until curtainsOpen=true */}
      <div className={`curtains ${curtainsOpen ? 'open' : ''}`}>
        <div className="curtain curtain-left" />
        <div className="curtain curtain-right" />
      </div>
    </div>
  );
}

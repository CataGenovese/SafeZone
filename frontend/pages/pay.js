import { useState } from 'react';
import axios from 'axios';

export default function Pay() {
  const [amount, setAmount] = useState('');
  const [status, setStatus] = useState(null);
  const [showOtp, setShowOtp] = useState(false);
  const [otpValue, setOtpValue] = useState('');

  const doPay = async () => {
    try {
      const resp = await axios.post('/api/fraud', {
        scenario: 'pay',
        amount: parseFloat(amount) || 0,
        location: 'Home',
      });
      const action = resp.data.action;
      setStatus(action);
      if (action === 'otp') setShowOtp(true);
    } catch (e) {
      setStatus('block');
    }
  };

  const verifyOtp = () => {
    setShowOtp(false);
    setStatus('ok');
  };

  return (
    <div className="app-container">
      <div className="card">
        <img src="/logo.png" className="logo" alt="logo" />
        <h1>Pago online</h1>
        <input
          placeholder="Importe"
          value={amount}
          onChange={e => setAmount(e.target.value)}
        />
        <button onClick={doPay}>Pagar</button>

        {status === 'ok' && <div className="status ok">Pago completado</div>}
        {status === 'otp' && <div className="status otp">OTP requerido</div>}
        {status === 'block' && <div className="status block">Pago bloqueado</div>}

        {showOtp && (
          <div style={{ marginTop: 12 }}>
            <input
              placeholder="Introduce OTP"
              value={otpValue}
              onChange={e => setOtpValue(e.target.value)}
            />
            <button onClick={verifyOtp} style={{ marginTop: 8 }}>
              Verificar OTP
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

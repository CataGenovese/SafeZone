import { useState } from 'react';
import axios from 'axios';

export default function Transfer() {
  const [account, setAccount] = useState('');
  const [amount, setAmount] = useState('');
  const [status, setStatus] = useState(null);
  const [showOtp, setShowOtp] = useState(false);
  const [otpValue, setOtpValue] = useState('');

  const doTransfer = async () => {
    try{
      const resp = await axios.post('/api/fraud', {
        scenario: 'transfer',
        amount: parseFloat(amount) || 0,
        location: 'Nigeria',
        deviceTrusted: false,
      });
      const action = resp.data.action;
      setStatus(action);
      if(action === 'otp') setShowOtp(true);
    }catch(e){
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
        <h1>Transferencia</h1>
        <p className="muted">Envía dinero de demo</p>
        <label>Cuenta destino</label>
        <input value={account} onChange={e => setAccount(e.target.value)} />
        <label>Importe</label>
        <input value={amount} onChange={e => setAmount(e.target.value)} />
        <button onClick={doTransfer}>Enviar</button>

        {status === 'ok' && <div className="status ok">Transferencia realizada</div>}
        {status === 'otp' && <div className="status otp">OTP requerido para completar</div>}
        {status === 'block' && <div className="status block">Transferencia bloqueada por riesgo</div>}

        {showOtp && (
          <div style={{marginTop:12}}>
            <input placeholder="Introduce OTP" value={otpValue} onChange={e=>setOtpValue(e.target.value)} />
            <button onClick={verifyOtp} style={{marginTop:8}}>Verificar OTP</button>
          </div>
        )}
      </div>
    </div>
  );
}

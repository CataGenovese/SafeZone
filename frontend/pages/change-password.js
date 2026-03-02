import { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router';

export default function ChangePassword() {
  const router = useRouter();
  const [pass, setPass] = useState('');
  const [status, setStatus] = useState(null);
  const [showOtp, setShowOtp] = useState(false);
  const [otpValue, setOtpValue] = useState('');

  const doChange = async () => {
    try{
      const resp = await axios.post('/api/fraud', {
        scenario: 'password',
        simSwap: true,
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
        <img
          src="/logo.png"
          className="logo"
          alt="logo"
          style={{width:100, borderRadius:20, cursor:'pointer'}}
          onClick={() => router.push('/dashboard')}
        />
        <h1>Cambiar contraseña</h1>
        <p className="muted">Simula un cambio de contraseña seguro</p>
        <input type="password" placeholder="Nueva contraseña" value={pass} onChange={e => setPass(e.target.value)} />
        <button onClick={doChange}>Cambiar</button>

        {status === 'ok' && <div className="status ok">Contraseña cambiada</div>}
        {status === 'otp' && <div className="status otp">OTP requerido para cambiar</div>}
        {status === 'block' && <div className="status block">Cambio bloqueado por riesgo</div>}

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

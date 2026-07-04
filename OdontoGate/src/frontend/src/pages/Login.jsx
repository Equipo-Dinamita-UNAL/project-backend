import { useState } from 'react';
import { loginUser, registerPatient } from '../api/authApi';

export default function Login({ onLoginSuccess }) {
    const [isRegisterMode, setIsRegisterMode] = useState(false);

    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const [regName, setRegName] = useState('');
    const [regLastname, setRegLastname] = useState('');
    const [regEmail, setRegEmail] = useState('');
    const [regPassword, setRegPassword] = useState('');
    const [regPhone, setRegPhone] = useState('');
    const [regBirthDate, setRegBirthDate] = useState('');
    const [regBloodType, setRegBloodType] = useState('');
    const [regAllergies, setRegAllergies] = useState('');
    const [regAddress, setRegAddress] = useState('');

    const inputStyle = {
        padding: '8px',
        borderRadius: '6px',
        border: '1px solid #cbd5e1'
    };

    const labelStyle = {
        fontSize: '12px',
        fontWeight: 'bold',
        color: '#475569',
        marginBottom: '4px'
    };

    const fieldStyle = {
        display: 'flex',
        flexDirection: 'column'
    };

    const resetRegisterForm = () => {
        setRegName('');
        setRegLastname('');
        setRegEmail('');
        setRegPassword('');
        setRegPhone('');
        setRegBirthDate('');
        setRegBloodType('');
        setRegAllergies('');
        setRegAddress('');
    };

    const handleLoginSubmit = (e) => {
        e.preventDefault();
        if (!email || !password) return;

        loginUser(email, password)
            .then((data) => {
                alert(`Bienvenido de nuevo, ${data.name || 'Usuario'}!`);
                if (onLoginSuccess) {
                    onLoginSuccess(data.role, data.userId);
                }
            })
            .catch((err) => {
                alert(err.message || 'Error de autenticacion. Verifica tu correo electronico y contrasena.');
            });
    };

    const handleRegisterSubmit = (e) => {
        e.preventDefault();
        if (!regName || !regLastname || !regEmail || !regPassword || !regPhone || !regBirthDate) {
            alert('Por favor completa todos los campos obligatorios (*)');
            return;
        }

        const payload = {
            name: regName.trim(),
            lastname: regLastname.trim(),
            email: regEmail.trim(),
            password: regPassword,
            phone: regPhone.trim(),
            birthDate: regBirthDate,
            bloodType: regBloodType || undefined,
            allergies: regAllergies.trim() || undefined,
            address: regAddress.trim() || undefined
        };

        registerPatient(payload)
            .then(() => {
                alert('Cuenta de paciente creada con exito. Ya puedes iniciar sesion con tus credenciales.');
                setIsRegisterMode(false);
                setEmail(regEmail);
                setPassword('');
                resetRegisterForm();
            })
            .catch((err) => {
                alert(err.message || 'Hubo un error al procesar el registro del paciente.');
            });
    };

    return (
        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '80vh', backgroundColor: '#f8fafc' }}>
            <div style={{ backgroundColor: 'white', padding: '35px', borderRadius: '8px', boxShadow: '0 4px 6px -1px rgba(0,0,0,0.1)', width: '100%', maxWidth: isRegisterMode ? '560px' : '420px' }}>

                <div style={{ textAlign: 'center', marginBottom: '25px' }}>
                    <h2 style={{ margin: 0, color: '#03045e', fontSize: '26px' }}>OdontoGate</h2>
                    <p style={{ margin: '5px 0 0 0', color: '#64748b', fontSize: '14px' }}>
                        {isRegisterMode ? 'Crea tu cuenta de paciente en la plataforma clinica' : 'Accede de forma segura a tu panel medico'}
                    </p>
                </div>

                {!isRegisterMode ? (
                    <form onSubmit={handleLoginSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                        <div style={{ display: 'flex', flexDirection: 'column' }}>
                            <label style={{ fontSize: '13px', fontWeight: 'bold', color: '#475569', marginBottom: '5px' }}>Correo Electronico</label>
                            <input type="email" placeholder="ejemplo@odontogate.com" value={email} onChange={(e) => setEmail(e.target.value)} style={{ padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e1' }} required />
                        </div>

                        <div style={{ display: 'flex', flexDirection: 'column' }}>
                            <label style={{ fontSize: '13px', fontWeight: 'bold', color: '#475569', marginBottom: '5px' }}>Contrasena</label>
                            <input type="password" placeholder="Minimo 6 caracteres" value={password} onChange={(e) => setPassword(e.target.value)} style={{ padding: '10px', borderRadius: '6px', border: '1px solid #cbd5e1' }} required />
                        </div>

                        <button type="submit" style={{ backgroundColor: '#0077b6', color: 'white', border: 'none', padding: '12px', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer', fontSize: '15px', marginTop: '5px' }}>
                            Ingresar al Sistema
                        </button>
                    </form>
                ) : (
                    <form onSubmit={handleRegisterSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '10px' }}>
                            <div style={fieldStyle}>
                                <label style={labelStyle}>Nombre *</label>
                                <input type="text" value={regName} onChange={(e) => setRegName(e.target.value)} style={inputStyle} required />
                            </div>
                            <div style={fieldStyle}>
                                <label style={labelStyle}>Apellido *</label>
                                <input type="text" value={regLastname} onChange={(e) => setRegLastname(e.target.value)} style={inputStyle} required />
                            </div>
                        </div>

                        <div style={fieldStyle}>
                            <label style={labelStyle}>Correo Electronico *</label>
                            <input type="email" placeholder="nombre@correo.com" value={regEmail} onChange={(e) => setRegEmail(e.target.value)} style={inputStyle} required />
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '10px' }}>
                            <div style={fieldStyle}>
                                <label style={labelStyle}>Contrasena de Acceso *</label>
                                <input type="password" placeholder="Minimo 6 caracteres" value={regPassword} onChange={(e) => setRegPassword(e.target.value)} style={inputStyle} minLength={6} required />
                            </div>
                            <div style={fieldStyle}>
                                <label style={labelStyle}>Telefono de Contacto *</label>
                                <input type="text" placeholder="Ej: 3001234567" value={regPhone} onChange={(e) => setRegPhone(e.target.value)} style={inputStyle} required />
                            </div>
                        </div>

                        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))', gap: '10px' }}>
                            <div style={fieldStyle}>
                                <label style={labelStyle}>Fecha de Nacimiento *</label>
                                <input type="date" value={regBirthDate} onChange={(e) => setRegBirthDate(e.target.value)} style={inputStyle} required />
                            </div>
                            <div style={fieldStyle}>
                                <label style={labelStyle}>Tipo de Sangre</label>
                                <select value={regBloodType} onChange={(e) => setRegBloodType(e.target.value)} style={inputStyle}>
                                    <option value="">Selecciona una opcion</option>
                                    <option value="A+">A+</option>
                                    <option value="A-">A-</option>
                                    <option value="B+">B+</option>
                                    <option value="B-">B-</option>
                                    <option value="AB+">AB+</option>
                                    <option value="AB-">AB-</option>
                                    <option value="O+">O+</option>
                                    <option value="O-">O-</option>
                                </select>
                            </div>
                        </div>

                        <div style={fieldStyle}>
                            <label style={labelStyle}>Direccion</label>
                            <input type="text" placeholder="Direccion de residencia" value={regAddress} onChange={(e) => setRegAddress(e.target.value)} style={inputStyle} />
                        </div>

                        <div style={fieldStyle}>
                            <label style={labelStyle}>Alergias</label>
                            <textarea placeholder="Ej: penicilina, anestesia local, latex. Escribe Ninguna si no aplica." value={regAllergies} onChange={(e) => setRegAllergies(e.target.value)} style={{ ...inputStyle, minHeight: '64px', resize: 'vertical' }} />
                        </div>

                        <button type="submit" style={{ backgroundColor: '#10b981', color: 'white', border: 'none', padding: '10px', borderRadius: '6px', fontWeight: 'bold', cursor: 'pointer', marginTop: '5px' }}>
                            Crear Cuenta de Paciente
                        </button>
                    </form>
                )}

                <div style={{ marginTop: '20px', textAlign: 'center', borderTop: '1px solid #e2e8f0', paddingTop: '15px' }}>
                    <button
                        type="button"
                        onClick={() => setIsRegisterMode(!isRegisterMode)}
                        style={{ background: 'none', border: 'none', color: '#0077b6', cursor: 'pointer', fontSize: '13px', fontWeight: 'bold', textDecoration: 'underline' }}
                    >
                        {isRegisterMode ? 'Ya tienes una cuenta? Inicia sesion aqui' : 'No tienes cuenta en la clinica? Registrate aqui'}
                    </button>
                </div>

            </div>
        </div>
    );
}

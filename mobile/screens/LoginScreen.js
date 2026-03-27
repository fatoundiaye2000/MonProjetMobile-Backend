// screens/LoginScreen.js
// ✅ Backend renvoie : { roles: [...], token: "..." } — PAS de userId
// ✅ Fix : après login, on appelle /api/users/all avec le token pour trouver idUser
import React, { useState, useEffect, useRef } from 'react';
import {
  View, Text, TextInput, TouchableOpacity, StyleSheet,
  Animated, Dimensions, KeyboardAvoidingView, Platform,
  ActivityIndicator, StatusBar,
} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import BASE_URL from '../services/api';

const { width, height } = Dimensions.get('window');
const ORANGE = '#F97316';
const BLUE   = '#2563EB';

// Comptes de test pour valider rapidement le TP
const ADMIN_EMAIL = 'admin@example.com';
const ADMIN_PASSWORD = 'Admin123!';
const USER_EMAIL = 'user@example.com';
const USER_PASSWORD = 'User123!';

// ─── Particule ────────────────────────────────────────────────────────────────
function Particle({ sx, sy, size, color, duration, delay }) {
  const px = useRef(new Animated.Value(sx)).current;
  const py = useRef(new Animated.Value(sy)).current;
  const op = useRef(new Animated.Value(0)).current;
  useEffect(() => {
    const loop = () => {
      px.setValue(sx + (Math.random() - 0.5) * 30);
      py.setValue(sy); op.setValue(0);
      Animated.sequence([
        Animated.delay(delay),
        Animated.timing(op, { toValue: 0.85, duration: 450, useNativeDriver: true }),
        Animated.parallel([
          Animated.timing(py, { toValue: sy - 110 - Math.random() * 90, duration, useNativeDriver: true }),
          Animated.timing(px, { toValue: sx + (Math.random() - 0.5) * 90, duration, useNativeDriver: true }),
        ]),
        Animated.timing(op, { toValue: 0, duration: 400, useNativeDriver: true }),
      ]).start(loop);
    };
    loop();
  }, []);
  return (
    <Animated.View style={{
      position: 'absolute', left: 0, top: 0,
      width: size, height: size, borderRadius: size / 2,
      backgroundColor: color, opacity: op,
      transform: [{ translateX: px }, { translateY: py }],
    }} />
  );
}

const PARTS = [
  { sx: width*0.04,  sy: height*0.87, size: 10, color: ORANGE,    duration: 3000, delay: 0    },
  { sx: width*0.16,  sy: height*0.91, size: 6,  color: BLUE,      duration: 3800, delay: 300  },
  { sx: width*0.28,  sy: height*0.85, size: 12, color: '#FB923C', duration: 3200, delay: 700  },
  { sx: width*0.42,  sy: height*0.90, size: 7,  color: BLUE,      duration: 2900, delay: 100  },
  { sx: width*0.56,  sy: height*0.86, size: 9,  color: ORANGE,    duration: 4000, delay: 500  },
  { sx: width*0.68,  sy: height*0.92, size: 6,  color: '#93C5FD', duration: 3500, delay: 900  },
  { sx: width*0.80,  sy: height*0.84, size: 8,  color: BLUE,      duration: 2700, delay: 200  },
  { sx: width*0.92,  sy: height*0.89, size: 5,  color: ORANGE,    duration: 3700, delay: 1100 },
  { sx: width*0.12,  sy: height*0.77, size: 5,  color: '#FDBA74', duration: 4200, delay: 1400 },
  { sx: width*0.60,  sy: height*0.79, size: 7,  color: BLUE,      duration: 3100, delay: 600  },
  { sx: width*0.36,  sy: height*0.75, size: 4,  color: ORANGE,    duration: 3600, delay: 1600 },
  { sx: width*0.75,  sy: height*0.78, size: 6,  color: '#60A5FA', duration: 2900, delay: 800  },
];

export default function LoginScreen({ navigation }) {
  const [email,    setEmail]    = useState('');
  const [password, setPassword] = useState('');
  const [loading,  setLoading]  = useState(false);
  const [errMsg,   setErrMsg]   = useState('');
  const [eFocus,   setEFocus]   = useState(false);
  const [pFocus,   setPFocus]   = useState(false);
  const [showPwd,  setShowPwd]  = useState(false);

  const logoOp = useRef(new Animated.Value(0)).current;
  const logoY  = useRef(new Animated.Value(-24)).current;
  const cardOp = useRef(new Animated.Value(0)).current;
  const cardY  = useRef(new Animated.Value(32)).current;
  const btnSc  = useRef(new Animated.Value(1)).current;

  useEffect(() => {
    Animated.sequence([
      Animated.parallel([
        Animated.timing(logoOp, { toValue: 1, duration: 550, useNativeDriver: true }),
        Animated.spring(logoY,  { toValue: 0, friction: 8,   useNativeDriver: true }),
      ]),
      Animated.parallel([
        Animated.timing(cardOp, { toValue: 1, duration: 450, useNativeDriver: true }),
        Animated.spring(cardY,  { toValue: 0, friction: 8,   useNativeDriver: true }),
      ]),
    ]).start();
  }, []);

  // ── Connexion ──────────────────────────────────────────────────────────────
  const handleLogin = async (override = {}) => {
    const emailToUse = (override.email ?? email).trim();
    const passwordToUse = override.password ?? password;

    // Mettez aussi à jour l'affichage (utile pour voir quel compte se connecte)
    if (override.email !== undefined) setEmail(String(override.email));
    if (override.password !== undefined) setPassword(String(override.password));

    if (!emailToUse || !passwordToUse) {
      setErrMsg('Veuillez remplir tous les champs.');
      return;
    }
    setErrMsg('');
    setLoading(true);

    try {
      // ── ÉTAPE 1 : POST /login → récupère { token, roles } ─────────────────
      const loginRes = await fetch(`${BASE_URL}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({ email: emailToUse, password: passwordToUse }),
      });

      const loginText = await loginRes.text();

      if (!loginRes.ok) {
        let msg = 'Email ou mot de passe incorrect';
        try {
          const json = JSON.parse(loginText);
          msg = json.message || json.error || msg;
        } catch { if (loginText.length < 150) msg = loginText; }
        setErrMsg(msg);
        setLoading(false);
        return;
      }

      let loginData;
      try { loginData = JSON.parse(loginText); }
      catch {
        setErrMsg('Réponse inattendue du serveur.');
        setLoading(false);
        return;
      }

      // ✅ Le backend renvoie : { token: "...", roles: ["ADMIN", "USER"] }
      const token = loginData.token || loginData.accessToken || loginData.jwt;
      if (!token) {
        setErrMsg('Token non reçu du serveur.');
        setLoading(false);
        return;
      }

      const roles = loginData.roles || loginData.authorities || [];

      // ── ÉTAPE 2 : GET /api/users/all pour trouver idUser par email ─────────
      // (car le backend ne renvoie pas userId dans la réponse login)
      let userId = '';
      try {
        const usersRes = await fetch(`${BASE_URL}/api/users/all`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        if (usersRes.ok) {
          const users = await usersRes.json();
          // Chercher l'utilisateur dont l'email correspond
          const found = Array.isArray(users)
            ? users.find(u =>
                (u.email || '').toLowerCase() === emailToUse.toLowerCase()
              )
            : null;
          if (found) {
            // Accepte idUser, id, userId selon la structure de ton entité User
            userId = String(found.idUser || found.id || found.userId || '');
          }
        }
      } catch (e) {
        // Si /api/users/all échoue (403 ou autre), on continue sans userId
        // Le bouton Participer affichera "non connecté" mais le login fonctionnera
        console.warn('Impossible de récupérer userId:', e.message);
      }

      // ── ÉTAPE 3 : Tout sauvegarder dans AsyncStorage ───────────────────────
      await AsyncStorage.setItem('token',     token);
      await AsyncStorage.setItem('userId',    userId);          // idUser de la BDD
      await AsyncStorage.setItem('userEmail', emailToUse);
      await AsyncStorage.setItem('userRoles', JSON.stringify(roles));

      console.log('✅ Login réussi');
      console.log('✅ userId:', userId);
      console.log('✅ roles:', roles);

      // ── ÉTAPE 4 : Navigation vers la liste ────────────────────────────────
      navigation.replace('EventsList');

    } catch (e) {
      setErrMsg('Serveur inaccessible.\nVérifiez que le backend est démarré et que l\'IP dans api.js est correcte.');
      setLoading(false);
    }
  };

  return (
    <View style={s.root}>
      <StatusBar barStyle="dark-content" backgroundColor="#FFF7ED" />

      {/* Fond pastel orange */}
      <View style={s.topBg} />
      <View style={[s.deco, { width: 240, height: 240, top: -80,  right: -70, backgroundColor: ORANGE, opacity: 0.07 }]} />
      <View style={[s.deco, { width: 160, height: 160, top:  60,  left:  -50, backgroundColor: BLUE,   opacity: 0.06 }]} />

      {PARTS.map((p, i) => <Particle key={i} {...p} />)}
      <View style={s.bottomLine} />

      <KeyboardAvoidingView style={s.kav} behavior={Platform.OS === 'ios' ? 'padding' : 'height'}>

        {/* Logo */}
        <Animated.View style={[s.logoArea, { opacity: logoOp, transform: [{ translateY: logoY }] }]}>
          <View style={s.logoBox}>
            <Text style={s.logoE}>E</Text>
          </View>
          <Text style={s.appName}>Evenix</Text>
          <Text style={s.appSub}>Découvrez · Réservez · Vivez</Text>
        </Animated.View>

        {/* Card */}
        <Animated.View style={[s.card, { opacity: cardOp, transform: [{ translateY: cardY }] }]}>
          <Text style={s.cardTitle}>Connexion</Text>
          <Text style={s.cardSub}>Entrez vos identifiants pour continuer</Text>

          {/* Erreur */}
          {errMsg ? (
            <View style={s.errBox}>
              <Text style={s.errIco}>⚠️</Text>
              <Text style={s.errTxt}>{errMsg}</Text>
            </View>
          ) : null}

          {/* Email */}
          <Text style={s.label}>EMAIL</Text>
          <View style={[s.field, eFocus && s.fieldOn]}>
            <Text style={s.fIco}>✉️</Text>
            <TextInput
              style={s.fInput}
              placeholder="admin@example.com"
              placeholderTextColor="#FED7AA"
              value={email}
              onChangeText={t => { setEmail(t); setErrMsg(''); }}
              keyboardType="email-address"
              autoCapitalize="none"
              autoCorrect={false}
              spellCheck={false}
              onFocus={() => setEFocus(true)}
              onBlur={() => setEFocus(false)}
            />
          </View>

          {/* Mot de passe */}
          <Text style={s.label}>MOT DE PASSE</Text>
          <View style={[s.field, pFocus && s.fieldOn]}>
            <Text style={s.fIco}>🔑</Text>
            <TextInput
              style={[s.fInput, { flex: 1 }]}
              placeholder="••••••••"
              placeholderTextColor="#FED7AA"
              value={password}
              onChangeText={t => { setPassword(t); setErrMsg(''); }}
              secureTextEntry={!showPwd}
              autoCapitalize="none"
              autoCorrect={false}
              spellCheck={false}
              onFocus={() => setPFocus(true)}
              onBlur={() => setPFocus(false)}
            />
            <TouchableOpacity onPress={() => setShowPwd(v => !v)} style={{ padding: 8 }}>
              <Text style={{ fontSize: 16 }}>{showPwd ? '🙈' : '👁️'}</Text>
            </TouchableOpacity>
          </View>

          {/* Bouton */}
          <Animated.View style={{ transform: [{ scale: btnSc }], marginTop: 6 }}>
            <TouchableOpacity
              style={[s.btn, loading && { opacity: 0.65 }]}
              onPress={() => handleLogin()}
              onPressIn={() => Animated.spring(btnSc, { toValue: 0.96, useNativeDriver: true }).start()}
              onPressOut={() => Animated.spring(btnSc, { toValue: 1, friction: 4, useNativeDriver: true }).start()}
              disabled={loading}
              activeOpacity={1}
            >
              {loading
                ? <ActivityIndicator color="white" />
                : <Text style={s.btnTxt}>Se connecter  →</Text>
              }
            </TouchableOpacity>
          </Animated.View>

          {/* Connexions rapides (auto) */}
          <View style={s.quickWrap}>
            <TouchableOpacity
              style={[s.quickBtn, s.quickAdmin]}
              onPress={() => handleLogin({ email: ADMIN_EMAIL, password: ADMIN_PASSWORD })}
              disabled={loading}
              activeOpacity={1}
            >
              <Text style={s.quickBtnTxt}>Connexion Admin</Text>
              <Text style={s.quickSubTxt}>{ADMIN_EMAIL}</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={[s.quickBtn, s.quickUser, s.quickBtnSecond]}
              onPress={() => handleLogin({ email: USER_EMAIL, password: USER_PASSWORD })}
              disabled={loading}
              activeOpacity={1}
            >
              <Text style={s.quickBtnTxt}>Connexion User</Text>
              <Text style={s.quickSubTxt}>{USER_EMAIL}</Text>
            </TouchableOpacity>
          </View>

          <View style={s.sep}>
            <View style={s.sepLine} />
            <Text style={s.sepTxt}>EVENIX</Text>
            <View style={s.sepLine} />
          </View>
          <Text style={s.hint}>Connectez-vous avec votre compte Evenix</Text>
        </Animated.View>
      </KeyboardAvoidingView>
    </View>
  );
}

const s = StyleSheet.create({
  root: { flex: 1, backgroundColor: '#FFF7ED' },
  kav:  { flex: 1, justifyContent: 'center', alignItems: 'center', paddingHorizontal: 22 },
  deco: { position: 'absolute', borderRadius: 999 },

  topBg: {
    position: 'absolute', top: 0, left: 0, right: 0,
    height: height * 0.42,
    backgroundColor: '#FFEDD5',
    borderBottomLeftRadius: 56, borderBottomRightRadius: 56,
  },
  bottomLine: {
    position: 'absolute', bottom: 0, left: 0, right: 0,
    height: 3, backgroundColor: ORANGE, opacity: 0.35,
  },

  logoArea: { alignItems: 'center', marginBottom: 32 },
  logoBox: {
    width: 66, height: 66, borderRadius: 20, backgroundColor: ORANGE,
    justifyContent: 'center', alignItems: 'center', marginBottom: 12,
    shadowColor: ORANGE, shadowOffset: { width: 0, height: 8 },
    shadowOpacity: 0.38, shadowRadius: 14, elevation: 10,
  },
  logoE:   { fontSize: 32, fontWeight: '900', color: 'white' },
  appName: { fontSize: 28, fontWeight: '900', color: '#1C0A00', letterSpacing: 0.6 },
  appSub:  { fontSize: 12, color: '#EA580C', marginTop: 5, letterSpacing: 0.8 },

  card: {
    width: '100%', backgroundColor: '#FFFFFF', borderRadius: 26, padding: 24,
    shadowColor: ORANGE, shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.1, shadowRadius: 22, elevation: 10,
    borderWidth: 1, borderColor: '#FEE8D6',
  },
  cardTitle: { fontSize: 20, fontWeight: '800', color: '#1C0A00', marginBottom: 4 },
  cardSub:   { fontSize: 13, color: '#9CA3AF', marginBottom: 18 },

  errBox: {
    flexDirection: 'row', alignItems: 'flex-start',
    backgroundColor: '#FFF1F2', borderRadius: 12,
    padding: 12, marginBottom: 16,
    borderWidth: 1, borderColor: '#FECDD3',
  },
  errIco: { fontSize: 15, marginRight: 8, marginTop: 1 },
  errTxt: { flex: 1, fontSize: 13, color: '#BE123C', fontWeight: '600', lineHeight: 18 },

  label: { fontSize: 10, fontWeight: '800', color: '#FB923C', letterSpacing: 1.3, marginBottom: 7 },
  field: {
    flexDirection: 'row', alignItems: 'center',
    backgroundColor: '#FAFAFA', borderRadius: 13, paddingHorizontal: 13,
    height: 52, marginBottom: 16,
    borderWidth: 1.5, borderColor: '#FED7AA',
  },
  fieldOn: {
    borderColor: ORANGE, backgroundColor: '#FFFBF8',
    shadowColor: ORANGE, shadowOffset: { width: 0, height: 0 },
    shadowOpacity: 0.18, shadowRadius: 8, elevation: 4,
  },
  fIco:   { fontSize: 15, marginRight: 9 },
  fInput: { flex: 1, fontSize: 15, color: '#1C0A00', fontWeight: '500' },

  btn: {
    height: 54, borderRadius: 15, backgroundColor: ORANGE,
    justifyContent: 'center', alignItems: 'center',
    shadowColor: ORANGE, shadowOffset: { width: 0, height: 6 },
    shadowOpacity: 0.4, shadowRadius: 13, elevation: 10,
  },
  btnTxt: { fontSize: 16, fontWeight: '800', color: 'white', letterSpacing: 0.4 },

  quickWrap: { marginTop: 14 },
  quickBtn: {
    paddingVertical: 12,
    paddingHorizontal: 14,
    borderRadius: 14,
    borderWidth: 1,
    backgroundColor: '#FFFFFF',
    borderColor: '#FEE8D6',
  },
  quickAdmin: { borderColor: ORANGE, backgroundColor: '#FFF1F0' },
  quickUser: { borderColor: '#93C5FD', backgroundColor: '#EFF6FF' },
  quickBtnTxt: { fontSize: 14, fontWeight: '900', color: '#1C0A00' },
  quickSubTxt: { marginTop: 4, fontSize: 12, fontWeight: '700', color: '#6B7280' },
  quickBtnSecond: { marginTop: 10 },

  sep:     { flexDirection: 'row', alignItems: 'center', marginTop: 22, marginBottom: 12 },
  sepLine: { flex: 1, height: 1, backgroundColor: '#FEE8D6' },
  sepTxt:  { fontSize: 10, color: '#FDBA74', marginHorizontal: 10, fontWeight: '700', letterSpacing: 1.5 },
  hint:    { textAlign: 'center', fontSize: 12, color: '#FDBA74', lineHeight: 17 },
});
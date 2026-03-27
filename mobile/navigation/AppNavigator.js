// navigation/AppNavigator.js
// ✅ Ajout de FavoritesScreen dans la navigation
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import LoginScreen      from '../screens/LoginScreen';
import EventsListScreen from '../screens/EventsListScreen';
import EventDetailScreen from '../screens/EventDetailScreen';
import FavoritesScreen  from '../screens/FavoritesScreen';

const Stack = createNativeStackNavigator();

export default function AppNavigator() {
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="Login"
        screenOptions={{
          headerStyle:     { backgroundColor: '#F97316' },
          headerTintColor: '#FFFFFF',
          headerTitleStyle:{ fontWeight: '800', fontSize: 17 },
        }}
      >
        {/* Login — sans header */}
        <Stack.Screen
          name="Login"
          component={LoginScreen}
          options={{ headerShown: false }}
        />

        {/* Liste des événements */}
        <Stack.Screen
          name="EventsList"
          component={EventsListScreen}
          options={{
            title: 'Événements',
            headerBackVisible: false, // pas de retour vers Login
          }}
        />

        {/* Détail d'un événement */}
        <Stack.Screen
          name="EventDetail"
          component={EventDetailScreen}
          options={{ title: 'Détail' }}
        />

        {/* Mes Favoris */}
        <Stack.Screen
          name="Favorites"
          component={FavoritesScreen}
          options={{ title: '❤️  Mes Favoris' }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
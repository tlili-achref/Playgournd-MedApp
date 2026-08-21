<script setup>
import { ref, computed } from 'vue'
import {
  Pencil,
  Check,
  Loader2,
  Mail,
  Phone,
  Stethoscope,
  Shield,
  Lock,
  CheckCircle2
} from 'lucide-vue-next'
import { useMedAppState } from '../../composables/useMedAppState.js'
import { useAuthStore } from '../../stores/authStore.js'
import { cn } from '../../lib/utils.js'

const authStore = useAuthStore()

// Notifications are disabled for now

const initials = computed(() => {
  const prenom = authStore.user?.prenom || ''
  const nom = authStore.user?.nom || ''
  if (prenom || nom) {
    return `${prenom.charAt(0)}${nom.charAt(0)}`.toUpperCase()
  }
  return authStore.user?.email?.split('@')[0].slice(0, 2).toUpperCase() || 'U'
})

const pf = ref({
  firstName: authStore.user?.prenom || authStore.user?.email?.split('@')[0] || 'Martin',
  lastName: authStore.user?.nom || 'Dr.',
  email: authStore.user?.email || 'dr.martin@medapp.fr',
  phone: '+33 6 12 34 56 78',
  specialty: authStore.role === 'medecin' ? 'Médecine générale' : 'Secrétariat médical',
})

const isDoctor = computed(() => authStore.role === 'medecin')

</script>

<template>
  <div class="p-6 space-y-6 max-w-2xl">
    <h1 class="text-2xl font-bold text-foreground">Paramètres</h1>

    <!-- Profile card -->
    <div class="rounded-2xl border border-border bg-card p-6">
      <div class="flex items-center justify-between mb-4">
        <h2 class="font-semibold text-foreground">Profil (Lecture seule)</h2>
        <span class="text-xs text-muted-foreground bg-muted px-2 py-1 rounded-md">De nouvelles mises à jour arrivent bientôt</span>
      </div>

      <!-- View mode only -->
      <div>
          <div class="flex items-center gap-4">
            <div class="w-16 h-16 rounded-full bg-gradient-to-br from-blue-400 to-blue-600 flex items-center justify-center text-white text-xl font-bold shrink-0">
              {{ initials }}
            </div>
            <div class="flex-1">
              <p class="font-semibold text-foreground capitalize">{{ authStore.user?.prenom || '' }} {{ authStore.user?.nom || '' }}</p>
              <p class="text-sm text-muted-foreground">{{ authStore.user?.email || 'dr.martin@medapp.fr' }}</p>
              <p class="text-xs text-muted-foreground mt-0.5">{{ pf.specialty }}</p>
            </div>
            <span v-if="saved" class="flex items-center gap-1.5 text-xs text-emerald-600 font-medium">
              <CheckCircle2 class="w-4 h-4" /> Enregistré
            </span>
          </div>
          <div class="grid grid-cols-2 gap-4 mt-5 text-sm">
            <div v-for="it in [
              { l: 'Téléphone', v: pf.phone, icon: Phone },
              { l: 'Spécialité', v: pf.specialty, icon: Stethoscope },
              ...(pf.rpps ? [{ l: 'N° RPPS', v: pf.rpps, icon: Shield }] : [])
            ]" :key="it.l">
              <p class="text-xs text-muted-foreground flex items-center gap-1 mb-0.5"><component :is="it.icon" class="w-3 h-3" />{{ it.l }}</p>
              <p class="font-medium text-foreground">{{ it.v }}</p>
            </div>
          </div>
        </div>
      </div>
    <!-- Preferences -->
    <div class="rounded-2xl border border-border bg-card p-6 text-center py-12">
      <div class="w-12 h-12 rounded-full bg-blue-100 dark:bg-blue-900/30 flex items-center justify-center mx-auto mb-4">
        <Info class="w-6 h-6 text-blue-600 dark:text-blue-400" />
      </div>
      <h2 class="font-semibold text-foreground text-lg mb-1">Préférences de notifications</h2>
      <p class="text-sm text-muted-foreground">De nouvelles mises à jour arrivent bientôt.</p>
    </div>
  </div>
</template>

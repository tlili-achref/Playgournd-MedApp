<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { Bell, Info } from 'lucide-vue-next'

const isDropdownOpen = ref(false)

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const closeDropdown = (e) => {
  if (!e.target.closest('.notification-container')) {
    isDropdownOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', closeDropdown)
})

onUnmounted(() => {
  document.removeEventListener('click', closeDropdown)
})
</script>

<template>
  <header class="h-16 flex items-center px-8 border-b border-sidebar-border bg-sidebar/50 backdrop-blur-md sticky top-0 z-40 shrink-0">
    <div class="flex-1"></div>
    
    <div class="flex items-center gap-2 notification-container relative">
      <button 
        @click="toggleDropdown"
        class="relative p-2 text-muted-foreground hover:bg-sidebar-accent hover:text-foreground rounded-xl transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-blue-500/50"
      >
        <Bell class="w-5 h-5" />
      </button>

      <!-- Dropdown -->
      <transition
        enter-active-class="transition ease-out duration-200"
        enter-from-class="opacity-0 translate-y-1"
        enter-to-class="opacity-100 translate-y-0"
        leave-active-class="transition ease-in duration-150"
        leave-from-class="opacity-100 translate-y-0"
        leave-to-class="opacity-0 translate-y-1"
      >
        <div v-if="isDropdownOpen" class="absolute top-full right-0 mt-2 w-80 bg-card border border-border rounded-2xl shadow-lg overflow-hidden flex flex-col max-h-96 z-50">
          <div class="p-4 border-b border-border flex items-center justify-between shrink-0 bg-muted/30">
            <h3 class="font-semibold text-foreground">Notifications</h3>
          </div>
          
          <div class="p-8 text-center flex flex-col items-center gap-3">
            <div class="w-12 h-12 rounded-full bg-blue-100 dark:bg-blue-900/30 flex items-center justify-center">
              <Info class="w-6 h-6 text-blue-600 dark:text-blue-400" />
            </div>
            <p class="font-medium text-foreground">De nouvelles mises à jour arrivent bientôt</p>
            <p class="text-xs text-muted-foreground">Le système de notifications est en cours de développement.</p>
          </div>
        </div>
      </transition>
    </div>
  </header>
</template>
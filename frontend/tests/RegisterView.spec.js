import { mount } from '@vue/test-utils'
import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import RegisterView from '../src/components/screens/RegisterView.vue'

describe('RegisterView.vue', () => {
  beforeEach(() => {
    vi.useFakeTimers()
  })

  afterEach(() => {
    vi.runOnlyPendingTimers()
    vi.useRealTimers()
  })

  const createWrapper = () => {
    return mount(RegisterView, {
      global: {
        directives: {
          motion: () => { }
        },
        stubs: {
          Stethoscope: true,
          ClipboardList: true,
          Shield: true,
          ChevronLeft: true,
          Mail: true,
          Lock: true,
          Loader2: true,
          Check: true,
          CheckCircle2: true
        }
      }
    })
  }

  // Helper to fill all fields with valid data
  const fillValidForm = async (wrapper) => {
    const inputs = wrapper.findAll('input')
    // firstName, lastName, email, password, confirm
    await inputs[0].setValue('Jean')
    await inputs[1].setValue('Martin')
    await inputs[2].setValue('jean.martin@clinique.fr')
    await inputs[3].setValue('motdepasse123')
    await inputs[4].setValue('motdepasse123')
  }

  it('renders register form by default', () => {
    const wrapper = createWrapper()
    expect(wrapper.text()).toContain('Créer un compte')
    expect(wrapper.find('form').exists()).toBe(true)
  })

  it('emits backToLogin event when clicking top Retour button', async () => {
    const wrapper = createWrapper()
    const buttons = wrapper.findAll('button')
    await buttons[0].trigger('click')
    expect(wrapper.emitted()).toHaveProperty('backToLogin')
  })

  it('emits backToLogin event when clicking bottom Annuler button', async () => {
    const wrapper = createWrapper()
    const buttons = wrapper.findAll('button')
    const annulerBtn = buttons.find(b => b.text().includes('Annuler'))
    await annulerBtn.trigger('click')
    expect(wrapper.emitted()).toHaveProperty('backToLogin')
  })

  // --- Tests de validation ---

  it('shows validation errors when submitting with all empty fields', async () => {
    const wrapper = createWrapper()
    await wrapper.find('form').trigger('submit')

    expect(wrapper.text()).toContain('Le prénom est requis.')
    expect(wrapper.text()).toContain('Le nom est requis.')
    expect(wrapper.text()).toContain("L'email est requis.")
    expect(wrapper.text()).toContain('Le mot de passe est requis.')
    expect(wrapper.text()).toContain('Veuillez confirmer le mot de passe.')
  })

  it('shows an error for invalid email format', async () => {
    const wrapper = createWrapper()
    const inputs = wrapper.findAll('input')
    await inputs[2].setValue('email-invalide')
    await wrapper.find('form').trigger('submit')

    expect(wrapper.text()).toContain("L'adresse email n'est pas valide.")
  })

  it('shows an error if password is too short', async () => {
    const wrapper = createWrapper()
    const inputs = wrapper.findAll('input')
    // Fill required fields to isolate the password-too-short error
    await inputs[0].setValue('Jean')
    await inputs[1].setValue('Martin')
    await inputs[2].setValue('jean.martin@clinique.fr')
    await inputs[3].setValue('abc') // password (too short, < 6 chars)
    await wrapper.find('form').trigger('submit')

    expect(wrapper.text()).toContain('Le mot de passe doit contenir au moins 6 caractères.')
  })

  it('shows an error if passwords do not match', async () => {
    const wrapper = createWrapper()
    const inputs = wrapper.findAll('input')
    await inputs[3].setValue('motdepasse123')   // password
    await inputs[4].setValue('autremotdepasse') // confirm (different)
    await wrapper.find('form').trigger('submit')

    expect(wrapper.text()).toContain('Les mots de passe ne correspondent pas.')
  })

  it('does not show success when form is invalid', async () => {
    const wrapper = createWrapper()
    await wrapper.find('form').trigger('submit')
    await vi.advanceTimersByTimeAsync(1400)

    // Success should not appear because the form is invalid
    expect(wrapper.text()).not.toContain('Demande envoyée !')
    expect(wrapper.find('form').exists()).toBe(true)
  })

  it('handles valid submission, shows success and allows return to login', async () => {
    const wrapper = createWrapper()

    // Ensure form is submitted
    await fillValidForm(wrapper)
    await wrapper.find('form').trigger('submit')

    // Should still be loading
    expect(wrapper.text()).not.toContain('Demande envoyée !')

    // Advance the timer (1400ms)
    await vi.advanceTimersByTimeAsync(1400)

    // Success message should now be visible
    expect(wrapper.text()).toContain('Demande envoyée !')
    expect(wrapper.find('form').exists()).toBe(false)

    // Click the "Retour à la connexion" button
    const returnBtn = wrapper.find('button')
    await returnBtn.trigger('click')

    expect(wrapper.emitted()).toHaveProperty('backToLogin')
  })
})

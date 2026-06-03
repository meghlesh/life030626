/* ================================================
   CWS.life — main.js
   Page specific JS — homepage animations
   ================================================ */

// ── Word Cycle Animation (Hero) ──
function initWordCycle() {
  const container = document.getElementById('wc');
  if (!container) return;

  const words = container.querySelectorAll('span');
  let current = 0;

  setInterval(() => {
    words[current].classList.remove('on');
    current = (current + 1) % words.length;
    words[current].classList.add('on');
  }, 2000);
}

// ── Contact Form Validation & Submission ──
function initContactForm() {
  const form = document.getElementById('contactForm');
  if (!form) return;
  
  // Prevent default form submission completely
  form.addEventListener('submit', function(e) {
    e.preventDefault();
    e.stopPropagation();
    return false;
  });
  
  const submitBtn = form.querySelector('.submit-btn-new');
  const firstNameInput = document.getElementById('firstname');
  const lastNameInput = document.getElementById('lastname');
  const emailInput = document.getElementById('email');
  const phoneInput = document.getElementById('phone');
  const messageInput = document.getElementById('message');
  
  // Helper function to show error message
  function showError(input, message) {
    removeError(input);
    
    const error = document.createElement('span');
    error.className = 'error-message';
    error.style.color = '#e74c3c';
    error.style.fontSize = '12px';
    error.style.marginTop = '5px';
    error.style.display = 'block';
    error.textContent = message;
    
    const wrapper = input.closest('.input-wrapper') || input.closest('.textarea-wrapper');
    if (wrapper) {
      wrapper.style.borderColor = '#e74c3c';
      wrapper.style.borderWidth = '1px';
      wrapper.style.borderStyle = 'solid';
    }
    input.style.borderColor = '#e74c3c';
    
    if (wrapper && wrapper.parentNode) {
      wrapper.parentNode.insertBefore(error, wrapper.nextSibling);
    } else {
      input.parentNode.insertBefore(error, input.nextSibling);
    }
  }
  
  function removeError(input) {
    const wrapper = input.closest('.input-wrapper') || input.closest('.textarea-wrapper');
    if (wrapper) {
      wrapper.style.borderColor = '';
      wrapper.style.borderWidth = '';
      wrapper.style.borderStyle = '';
    }
    input.style.borderColor = '';
    
    // Remove error message if exists
    const parent = input.parentNode;
    if (parent) {
      const error = parent.querySelector('.error-message');
      if (error) error.remove();
    }
    
    // Also check next sibling
    const nextError = input.parentNode.nextSibling;
    if (nextError && nextError.className === 'error-message') {
      nextError.remove();
    }
  }
  
  // Name validation - Only letters and spaces, no digits
  function validateName(input, fieldName) {
    const value = input.value.trim();
    const nameRegex = /^[A-Za-z\s\-']+$/; // Allows letters, spaces, hyphens, apostrophes
    
    if (!value) {
      showError(input, `${fieldName} is required`);
      return false;
    }
    if (value.length < 2) {
      showError(input, `${fieldName} must be at least 2 characters`);
      return false;
    }
    if (!nameRegex.test(value)) {
      showError(input, `${fieldName} should only contain letters (no numbers or special characters)`);
      return false;
    }
    removeError(input);
    return true;
  }
  
  function validateFirstName() {
    if (!firstNameInput) return true;
    return validateName(firstNameInput, 'First name');
  }
  
  function validateLastName() {
    if (!lastNameInput) return true;
    return validateName(lastNameInput, 'Last name');
  }
  
  function validateEmail() {
    if (!emailInput) return true;
    const value = emailInput.value.trim();
    const emailRegex = /^[^\s@]+@([^\s@]+\.)+[^\s@]+$/;
    
    if (!value) {
      showError(emailInput, 'Email address is required');
      return false;
    }
    if (!emailRegex.test(value)) {
      showError(emailInput, 'Please enter a valid email address (e.g., name@example.com)');
      return false;
    }
    removeError(emailInput);
    return true;
  }
  
  function validatePhone() {
    if (!phoneInput) return true;
    const value = phoneInput.value.trim();
    if (value) {
      const digitsOnly = value.replace(/\D/g, '');
      if (digitsOnly.length < 10) {
        showError(phoneInput, 'Please enter a valid phone number (minimum 10 digits)');
        return false;
      }
      if (digitsOnly.length > 15) {
        showError(phoneInput, 'Phone number is too long (maximum 15 digits)');
        return false;
      }
    }
    removeError(phoneInput);
    return true;
  }
  
  function validateMessage() {
    if (!messageInput) return true;
    const value = messageInput.value.trim();
    if (!value) {
      showError(messageInput, 'Message cannot be empty');
      return false;
    }
    if (value.length < 10) {
      showError(messageInput, 'Message must be at least 10 characters');
      return false;
    }
    if (value.length > 1000) {
      showError(messageInput, 'Message is too long (maximum 1000 characters)');
      return false;
    }
    removeError(messageInput);
    return true;
  }
  
  // Real-time validation
  if (firstNameInput) {
    firstNameInput.addEventListener('blur', validateFirstName);
    firstNameInput.addEventListener('input', validateFirstName);
  }
  if (lastNameInput) {
    lastNameInput.addEventListener('blur', validateLastName);
    lastNameInput.addEventListener('input', validateLastName);
  }
  if (emailInput) {
    emailInput.addEventListener('blur', validateEmail);
    emailInput.addEventListener('input', validateEmail);
  }
  if (phoneInput) {
    phoneInput.addEventListener('blur', validatePhone);
    phoneInput.addEventListener('input', validatePhone);
  }
  if (messageInput) {
    messageInput.addEventListener('blur', validateMessage);
    messageInput.addEventListener('input', validateMessage);
  }
  
  function showSuccessModal(title, message) {
    // Remove existing modal
    const existingModal = document.querySelector('.custom-modal-overlay');
    if (existingModal) existingModal.remove();
    
    const overlay = document.createElement('div');
    overlay.className = 'custom-modal-overlay';
    overlay.style.cssText = `
      position: fixed; top: 0; left: 0; width: 100%; height: 100%;
      background-color: rgba(0,0,0,0.5); display: flex; align-items: center;
      justify-content: center; z-index: 10001; backdrop-filter: blur(3px);
    `;
    
    const modal = document.createElement('div');
    modal.style.cssText = `
      background-color: #fff; border-radius: 12px; padding: 32px 40px;
      max-width: 450px; width: 90%; text-align: center;
      box-shadow: 0 20px 40px rgba(0,0,0,0.2);
    `;
    
    // Add animation
    modal.style.animation = 'modalFadeIn 0.3s ease';
    
    const checkIcon = document.createElement('div');
    checkIcon.style.cssText = `
      width: 70px; height: 70px; background-color: #0796FE; border-radius: 50%;
      display: flex; align-items: center; justify-content: center; margin: 0 auto 20px;
    `;
    checkIcon.innerHTML = '<i class="fas fa-check" style="font-size: 35px; color: #fff;"></i>';
    
    const titleEl = document.createElement('h3');
    titleEl.textContent = title;
    titleEl.style.cssText = 'font-family: Oswald, sans-serif; font-weight: 300; font-size: 28px; color: #0a2540; margin: 0 0 12px 0;';
    
    const messageEl = document.createElement('p');
    messageEl.textContent = message;
    messageEl.style.cssText = 'font-family: Lato, sans-serif; font-size: 16px; color: #797979; line-height: 1.5; margin: 0 0 24px 0;';
    
    const closeBtn = document.createElement('button');
    closeBtn.textContent = 'OK';
    closeBtn.style.cssText = `
      background-color: #0796FE; border: none; color: #fff; padding: 12px 30px;
      border-radius: 6px; font-family: DM Sans, sans-serif; font-size: 16px;
      font-weight: 500; cursor: pointer; transition: background-color 0.3s;
    `;
    closeBtn.onmouseenter = () => closeBtn.style.backgroundColor = '#0d3358';
    closeBtn.onmouseleave = () => closeBtn.style.backgroundColor = '#0796FE';
    closeBtn.onclick = () => overlay.remove();
    
    modal.appendChild(checkIcon);
    modal.appendChild(titleEl);
    modal.appendChild(messageEl);
    modal.appendChild(closeBtn);
    overlay.appendChild(modal);
    document.body.appendChild(overlay);
    
    // Close on overlay click
    overlay.onclick = (e) => { if (e.target === overlay) overlay.remove(); };
  }
  
  function showToast(message, isSuccess = true) {
    // Remove existing toast
    const existingToast = document.querySelector('.custom-toast');
    if (existingToast) existingToast.remove();
    
    const toast = document.createElement('div');
    toast.className = 'custom-toast';
    toast.style.cssText = `
      position: fixed; bottom: 30px; right: 30px; background-color: ${isSuccess ? '#0796FE' : '#e74c3c'};
      color: #fff; padding: 12px 20px; border-radius: 8px; font-family: DM Sans, sans-serif;
      font-size: 14px; font-weight: 500; z-index: 10000; box-shadow: 0 4px 12px rgba(0,0,0,0.15);
      animation: slideInRight 0.3s ease;
    `;
    toast.innerHTML = `<i class="fas ${isSuccess ? 'fa-check-circle' : 'fa-exclamation-circle'}" style="margin-right: 8px;" ></i>${message}`;
    document.body.appendChild(toast);
    
    setTimeout(() => {
      toast.style.animation = 'slideOutRight 0.3s ease';
      setTimeout(() => toast.remove(), 300);
    }, 3000);
  }
  
  async function handleSubmit(e) {
    e.preventDefault();
    e.stopPropagation();
    
    // Run all validations
    const isFirstNameValid = validateFirstName();
    const isLastNameValid = validateLastName();
    const isEmailValid = validateEmail();
    const isPhoneValid = validatePhone();
    const isMessageValid = validateMessage();
    
    if (isFirstNameValid && isLastNameValid && isEmailValid && isPhoneValid && isMessageValid) {
      // Disable button and show loading state
      const originalBtnText = submitBtn.innerHTML;
      submitBtn.disabled = true;
      submitBtn.style.opacity = '0.7';
      submitBtn.style.cursor = 'not-allowed';
      submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Sending...';
      
      // // Backend API call
      // const formData = {
      //   firstName: firstNameInput?.value.trim() || '',
      //   lastName:  lastNameInput?.value.trim()  || '',
      //   email:     emailInput?.value.trim()     || '',
      //   phone:     phoneInput?.value.trim()     || '',
      //   message:   messageInput?.value.trim()   || ''
      // };
      const formData = {
        firstName:       firstNameInput?.value.trim() || '',
        lastName:        lastNameInput?.value.trim()  || '',
        email:           emailInput?.value.trim()     || '',
        phone:           phoneInput?.value.trim()     || '',
        message:         messageInput?.value.trim()   || '',
        captchaToken:    grecaptcha.getResponse()     || ''
      };

      fetch('http://localhost:8080/api/contact/submit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      })
      .then(function(res) { return res.json(); })
      .then(function(data) {
        if (data.message === 'Message sent successfully!') {
          // Form reset
          form.reset();
          // Error messages remove
          [firstNameInput, lastNameInput, emailInput, phoneInput, messageInput].forEach(function(input) {
            if (input) removeError(input);
          });
          // Success modal
          showSuccessModal('Message Sent!', 'Thank you for contacting us. We will get back to you soon!');
          showToast('Your message has been sent successfully!', true);
          // Captcha reset karo
          if (typeof grecaptcha !== 'undefined') {
            grecaptcha.reset();
          }
        } else {
          showToast(data.message || 'Something went wrong.', false);
        }
        // Button re-enable
        submitBtn.disabled = false;
        submitBtn.style.opacity = '1';
        submitBtn.style.cursor = 'pointer';
        submitBtn.innerHTML = originalBtnText;
      })
      .catch(function(error) {
        console.log('Error:', error);
        showToast('Cannot connect to server. Make sure backend is running.', false);
        submitBtn.disabled = false;
        submitBtn.style.opacity = '1';
        submitBtn.style.cursor = 'pointer';
        submitBtn.innerHTML = originalBtnText;
      });
      
    } else {
      // Scroll to first error
      const firstError = document.querySelector('.error-message');
      if (firstError) {
        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
      }
      showToast('Please fix the errors before submitting.', false);
    }
  }
  
  // Attach submit handler
  form.addEventListener('submit', handleSubmit);
}

// Add animation keyframes to document
function addAnimationStyles() {
  if (document.getElementById('contact-form-animations')) return;
  
  const style = document.createElement('style');
  style.id = 'contact-form-animations';
  style.textContent = `
    @keyframes modalFadeIn {
      from {
        opacity: 0;
        transform: translateY(-30px) scale(0.95);
      }
      to {
        opacity: 1;
        transform: translateY(0) scale(1);
      }
    }
    
    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
    
    @keyframes slideInRight {
      from {
        transform: translateX(100%);
        opacity: 0;
      }
      to {
        transform: translateX(0);
        opacity: 1;
      }
    }
    
    @keyframes slideOutRight {
      from {
        transform: translateX(0);
        opacity: 1;
      }
      to {
        transform: translateX(100%);
        opacity: 0;
      }
    }
    
    .fa-spinner {
      animation: spin 1s linear infinite;
    }
    
    .error-message {
      color: #e74c3c;
      font-size: 12px;
      margin-top: 5px;
      display: block;
      font-family: 'Lato', sans-serif;
    }
    
    .submit-btn-new:disabled {
      opacity: 0.7;
      cursor: not-allowed;
    }
    
    .input-wrapper.error,
    .textarea-wrapper.error {
      border-color: #e74c3c !important;
    }
  `;
  document.head.appendChild(style);
}

// ================================================
// NEWSLETTER SUBSCRIPTION - WORKING VERSION
// ================================================
function initNewsletter() {
  console.log('Newsletter: Initializing...');
  
  const form = document.getElementById('newsletterForm');
  if (!form) {
    console.log('Newsletter: Form not found, retrying in 500ms...');
    setTimeout(initNewsletter, 500);
    return;
  }
  
  console.log('Newsletter: Form found! Setting up...');
  
  // Remove any existing submit handlers
  const newForm = form.cloneNode(true);
  form.parentNode.replaceChild(newForm, form);
  
  const finalForm = document.getElementById('newsletterForm');
  const emailInput = document.getElementById('newsletterEmail');
  const submitBtn = finalForm.querySelector('button');
  
  // Create message div if not exists
  let msgDiv = document.getElementById('newsletterMessage');
  if (!msgDiv) {
    msgDiv = document.createElement('div');
    msgDiv.id = 'newsletterMessage';
    msgDiv.style.marginTop = '15px';
    finalForm.parentNode.appendChild(msgDiv);
  }
  
  finalForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    e.stopPropagation();
    
    const email = emailInput.value.trim();
    const originalText = submitBtn.innerHTML;
    
    if (!email) {
      msgDiv.innerHTML = '<div style="padding:10px; background:#f8d7da; color:#721c24; border-radius:5px;">❌ Please enter your email address</div>';
      setTimeout(() => msgDiv.innerHTML = '', 3000);
      return;
    }
    
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
    if (!emailPattern.test(email)) {
      msgDiv.innerHTML = '<div style="padding:10px; background:#f8d7da; color:#721c24; border-radius:5px;">❌ Please enter a valid email address</div>';
      setTimeout(() => msgDiv.innerHTML = '', 3000);
      return;
    }
    
    // Show loading
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Subscribing...';
    msgDiv.innerHTML = '<div style="padding:10px; background:#d1ecf1; color:#0c5460; border-radius:5px;">⏳ Sending request...</div>';
    
    try {
      const response = await fetch('http://localhost:8080/api/email/subscribe', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email })
      });
      
      const result = await response.text();
      console.log('Newsletter Response:', result);
      
      if (response.ok) {
        emailInput.value = '';
        msgDiv.innerHTML = '<div style="padding:10px; background:#d4edda; color:#155724; border-radius:5px;">✅ ' + result + '</div>';
      } else {
        msgDiv.innerHTML = '<div style="padding:10px; background:#f8d7da; color:#721c24; border-radius:5px;">❌ ' + result + '</div>';
      }
    } catch (error) {
      console.error('Newsletter Error:', error);
      msgDiv.innerHTML = '<div style="padding:10px; background:#f8d7da; color:#721c24; border-radius:5px;">❌ Cannot connect to backend. Make sure backend is running on port 8080</div>';
    } finally {
      submitBtn.disabled = false;
      submitBtn.innerHTML = originalText;
      setTimeout(() => {
        if (msgDiv.innerHTML !== '') msgDiv.innerHTML = '';
      }, 5000);
    }
  });
  
  console.log('Newsletter: Event listener attached successfully!');
}

// ================================================
// RUN ON PAGE LOAD
// ================================================
document.addEventListener('DOMContentLoaded', () => {
  addAnimationStyles();
  initWordCycle();
  initContactForm();
  initNewsletter();  // This will handle newsletter
});
(function() {
  const emailField = document.getElementById('emailInput');
  const passwordField = document.getElementById('passwordInput');
  const loginButton = document.getElementById('loginBtn');
  const forgotLink = document.getElementById('forgotPasswordTrigger');
  const messageDiv = document.getElementById('demoMessage');
  const rememberCheckbox = document.getElementById('rememberCheckbox');
  const togglePassword = document.getElementById('togglePassword');
  const forgotPasswordModal = document.getElementById('forgotPasswordModal');
  // const sendEmailBtn = document.getElementById('sendEmailBtn');
  // const backToLoginLink = document.getElementById('backToLoginLink');
  // const forgotEmailInput = document.getElementById('forgotEmailInput');

  // Create validation message elements for login form
  const emailError = document.createElement('div');
  emailError.className = 'validation-message';
  emailError.style.cssText = 'color: #e53e3e; font-size: 12px; margin-top: 4px; display: none;';
  
  const passwordError = document.createElement('div');
  passwordError.className = 'validation-message';
  passwordError.style.cssText = 'color: #e53e3e; font-size: 12px; margin-top: 4px; display: none;';

  // Create validation message elements for forgot password modal
  const forgotEmailError = document.createElement('div');
  forgotEmailError.className = 'validation-message';
  forgotEmailError.style.cssText = 'color: #e53e3e; font-size: 12px; margin-top: 4px; display: none;';

  // Insert validation messages after input fields
  const emailGroup = document.querySelector('.input-group');
  if (emailField && emailGroup) {
    emailField.insertAdjacentElement('afterend', emailError);
  }

  const passwordWrapper = document.querySelector('.password-wrapper');
  if (passwordField && passwordWrapper) {
    passwordWrapper.insertAdjacentElement('afterend', passwordError);
  }

  // Insert validation message for forgot password modal
  const forgotEmailGroup = document.querySelector('#forgotPasswordModal .input-group');
  if (forgotEmailInput && forgotEmailGroup) {
    forgotEmailInput.insertAdjacentElement('afterend', forgotEmailError);
  }

  function handleBackgroundImage() {
    const bgDiv = document.querySelector('.bg-image');
    if (bgDiv) {
      const testImg = new Image();
      testImg.onload = function() {
        bgDiv.style.opacity = '0.2';
      };
      testImg.onerror = function() {
        bgDiv.style.opacity = '0';
      };
    //   testImg.src = '../assets/images/loginbg.png';
    }
  }

  function handleLogo() {
    const logo = document.querySelector('.logo-image');
    if (logo) {
      logo.onerror = () => {
        if (!logo.getAttribute('data-fallback-set')) {
          logo.setAttribute('data-fallback-set', 'true');
        }
      };
    }
  }

  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', function() {
      handleBackgroundImage();
      handleLogo();
    });
  } else {
    handleBackgroundImage();
    handleLogo();
  }

  function showToast(message, isError = true) {
    messageDiv.style.display = 'block';
    messageDiv.textContent = message;
    if (isError) {
      messageDiv.style.backgroundColor = '#fff5f5';
      messageDiv.style.color = '#c71f1f';
      messageDiv.style.borderLeft = '5px solid #e53e3e';
    } else {
      messageDiv.style.backgroundColor = '#e9f7eb';
      messageDiv.style.color = '#0c6b3f';
      messageDiv.style.borderLeft = '5px solid #0796FE';
    }
    messageDiv.style.borderRadius = '28px';
    messageDiv.style.fontWeight = '600';
    messageDiv.style.padding = '10px 16px';
    setTimeout(() => {
      messageDiv.style.display = 'none';
    }, 3300);
  }

  function validateEmail(email) {
    const emailRegex = /^[^\s@]+@([^\s@]+\.)+[^\s@]+$/;
    if (!email) {
      return { valid: false, message: 'Email address is required.' };
    }
    if (!emailRegex.test(email)) {
      return { valid: false, message: 'Please enter a valid email address (e.g., name@example.com).' };
    }
    return { valid: true, message: '' };
  }

  function validatePassword(password) {
    if (!password) {
      return { valid: false, message: 'Password is required.' };
    }
    if (password.length < 6) {
      return { valid: false, message: 'Password must be at least 6 characters long.' };
    }
    return { valid: true, message: '' };
  }

  function clearValidationMessages() {
    emailError.style.display = 'none';
    emailError.textContent = '';
    passwordError.style.display = 'none';
    passwordError.textContent = '';
    
    // Remove error styling from input fields
    emailField.style.borderColor = '#e2e8f0';
    passwordField.style.borderColor = '#e2e8f0';
  }

  function clearForgotValidationMessages() {
    forgotEmailError.style.display = 'none';
    forgotEmailError.textContent = '';
    if (forgotEmailInput) {
      forgotEmailInput.style.borderColor = '#e2e8f0';
    }
  }

  function showValidationError(field, errorElement, message) {
    errorElement.textContent = message;
    errorElement.style.display = 'block';
    field.style.borderColor = '#e53e3e';
  }

  function validateAndShowErrors() {
    clearValidationMessages();
    
    const email = emailField.value.trim();
    const password = passwordField.value;
    
    const emailValidation = validateEmail(email);
    const passwordValidation = validatePassword(password);
    
    let isValid = true;
    
    if (!emailValidation.valid) {
      showValidationError(emailField, emailError, emailValidation.message);
      isValid = false;
    }
    
    if (!passwordValidation.valid) {
      showValidationError(passwordField, passwordError, passwordValidation.message);
      isValid = false;
    }
    
    return isValid;
  }

  function validateForgotPasswordEmail() {
    clearForgotValidationMessages();
    
    const email = forgotEmailInput.value.trim();
    const emailValidation = validateEmail(email);
    
    if (!emailValidation.valid) {
      showValidationError(forgotEmailInput, forgotEmailError, emailValidation.message);
      return false;
    }
    
    return true;
  }

  function validateCredentials() {
    return validateAndShowErrors();
  }
/////// backend api login connectivity
function handleLogin() {
  if (!validateAndShowErrors()) {
    return;
  }

  const email    = emailField.value.trim();
  const password = passwordField.value;
  const remember = rememberCheckbox.checked;

  // Button disable karo loading ke time
  loginButton.disabled    = true;
  loginButton.textContent = 'Logging in...';

  // Backend API call
  fetch('https://api-lifeqa-be.azurewebsites.net/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      email:    email,
      password: password
    })
  })
  .then(function(res) { return res.json(); })
  .then(function(data) {

    if (data.token) {
      // Login successful
     
       // clear fields
       //  ADD THIS HERE (before storing anything)
      localStorage.removeItem('userToken');
      localStorage.removeItem('userName');
      localStorage.removeItem('userRole');
      localStorage.removeItem('userEmail');
      localStorage.removeItem('adminName');
      localStorage.removeItem('adminRole');
        emailField.value = '';
        passwordField.value = '';
      // localStorage.setItem('userToken', data.token);
      // localStorage.setItem('userName',  data.name);
      // localStorage.setItem('userRole',  data.role);
      // localStorage.setItem('userEmail', data.email);

      localStorage.setItem('user', JSON.stringify({
        name: data.name,
        email: data.email,
        role: data.role,
        token: data.token
      }));

      if (remember) {
        localStorage.setItem('creative_email',    email);
        localStorage.setItem('creative_remember', 'true');
      } else {
        localStorage.removeItem('creative_email');
        localStorage.setItem('creative_remember', 'false');
      }

      showToast('✨ Welcome back, ' + data.name + '! Access granted.', false);

      // Role check karke redirect karo
      setTimeout(function() {
        const user = JSON.parse(localStorage.getItem('user'));
        if (data.role === 'SUPER_ADMIN') {
          window.location.href = 'admin-dashboard.html';
        } else {
          window.location.href = 'index.html';
        }
      }, 1500);

    } else {
      // Login failed
      showToast('❌ ' + (data.message || 'Invalid email or password.'), true);
      loginButton.disabled    = false;
      loginButton.textContent = 'Login';
    }
  })
  .catch(function(error) {
    showToast('❌ Cannot connect to server. Make sure backend is running.', true);
    loginButton.disabled    = false;
    loginButton.textContent = 'Login';
    console.log('Error:', error);
  });
}/////// backend api login connectivity

  function loadRememberedUser() {
    const rememberFlag = localStorage.getItem('creative_remember');
    const savedEmail = localStorage.getItem('creative_email');
    if (rememberFlag === 'true' && savedEmail) {
      emailField.value = savedEmail;
      rememberCheckbox.checked = true;
    }
  }
//////forgot password api
 function onForgotPassword(e) {
  e.preventDefault();
  clearForgotValidationMessages();
  forgotPasswordModal.style.display = 'flex';
  document.querySelector('.login-card-outer').style.opacity = '0';
  document.querySelector('.login-card-outer').style.visibility = 'hidden';
  document.body.classList.add('modal-open');

  // Modal open hone ke baad button dhundho
  var sendBtn   = document.getElementById('sendEmailBtn');
  var emailInpt = document.getElementById('forgotEmailInput');
  var backBtn   = document.getElementById('backToLoginLink');

  // Back to login
  if (backBtn) {
    var newBackBtn = backBtn.cloneNode(true);
    backBtn.parentNode.replaceChild(newBackBtn, backBtn);
    newBackBtn.addEventListener('click', function(e) {
      e.preventDefault();
      closeModal();
    });
  }

  // Send Email button
  if (sendBtn) {
    // Clone karo purane listeners hatane ke liye
    var newSendBtn = sendBtn.cloneNode(true);
    sendBtn.parentNode.replaceChild(newSendBtn, sendBtn);

    newSendBtn.addEventListener('click', function() {
      var email = emailInpt ? emailInpt.value.trim() : '';

      if (!email || !email.includes('@')) {
        showToast('❌ Please enter a valid email address.', true);
        return;
      }

      newSendBtn.disabled    = true;
      newSendBtn.textContent = 'Sending...';

      fetch('https://api-lifeqa-be.azurewebsites.net/api/auth/forgot-password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email })
      })
      .then(function(res) { return res.json(); })
      .then(function(data) {
        if (data.message && data.message.includes('sent')) {
          closeModal();
          showToast('📩 Reset link sent to ' + email + '!', false);
        } else {
          showToast('❌ ' + (data.message || 'Email not found.'), true);
          newSendBtn.disabled    = false;
          newSendBtn.textContent = 'Send Email';
        }
      })
      .catch(function() {
        showToast('❌ Cannot connect to server.', true);
        newSendBtn.disabled    = false;
        newSendBtn.textContent = 'Send Email';
      });
    });
  }
}//////forgot password api

  function closeModal() {
    forgotPasswordModal.style.display = 'none';
    document.querySelector('.login-card-outer').style.opacity = '1';
    document.querySelector('.login-card-outer').style.visibility = 'visible';
    if (forgotEmailInput) {
      forgotEmailInput.value = '';
      clearForgotValidationMessages();
    }

    document.body.classList.remove('modal-open');
  }

  function setupEnterKey() {
    const inputs = [emailField, passwordField];
    inputs.forEach(input => {
      input.addEventListener('keypress', (event) => {
        if (event.key === 'Enter') {
          event.preventDefault();
          handleLogin();
        }
      });
    });
  }

  function clearToastOnInput() {
    if (messageDiv.style.display === 'block') {
      messageDiv.style.display = 'none';
    }
  }

  function clearValidationOnInput() {
    // Clear email validation when user starts typing
    if (emailError.style.display === 'block') {
      emailError.style.display = 'none';
      emailField.style.borderColor = '#e2e8f0';
    }
  }

  function clearPasswordValidationOnInput() {
    // Clear password validation when user starts typing
    if (passwordError.style.display === 'block') {
      passwordError.style.display = 'none';
      passwordField.style.borderColor = '#e2e8f0';
    }
  }

  function clearForgotValidationOnInput() {
    // Clear forgot password email validation when user starts typing
    if (forgotEmailError.style.display === 'block') {
      forgotEmailError.style.display = 'none';
      if (forgotEmailInput) {
        forgotEmailInput.style.borderColor = '#e2e8f0';
      }
    }
  }
  
  // Toggle password visibility
  if (togglePassword && passwordField) {
    togglePassword.addEventListener('click', function() {
      const type = passwordField.getAttribute('type') === 'password' ? 'text' : 'password';
      passwordField.setAttribute('type', type);
      
      const svg = this.querySelector('.eye-icon');
      if (type === 'text') {
        svg.innerHTML = '<path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 0 1-4.24-4.24"></path><line x1="1" y1="1" x2="23" y2="23"></line>';
      } else {
        svg.innerHTML = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle>';
      }
    });
  }

  loginButton.addEventListener('click', (e) => {
    e.preventDefault();
    handleLogin();
  });

  forgotLink.addEventListener('click', onForgotPassword);
  
  // backToLoginLink.addEventListener('click', (e) => {
  //   e.preventDefault();
  //   closeModal();
  // });
// //////Backend Api of Froget Password connectivity
//  sendEmailBtn.addEventListener('click', function() {
//   if (!validateForgotPasswordEmail()) {
//     showToast('❌ Please enter a valid email address.', true);
//     return;
//   }

//   const email = forgotEmailInput.value.trim();

//   sendEmailBtn.disabled    = true;
//   sendEmailBtn.textContent = 'Sending...';

//   // Backend API call
//   fetch('https://api-lifeqa-be.azurewebsites.net/api/auth/forgot-password', {
//     method: 'POST',
//     headers: { 'Content-Type': 'application/json' },
//     body: JSON.stringify({ email: email })
//   })
//   .then(function(res) { return res.json(); })
//   .then(function(data) {
//     if (data.message && data.message.includes('sent')) {
//       showToast('📩 Password reset link sent to ' + email + '!', false);
//       closeModal();
//     } else {
//       showToast('❌ ' + (data.message || 'Email not found.'), true);
//       sendEmailBtn.disabled    = false;
//       sendEmailBtn.textContent = 'Send Email';
//     }
//   })
//   .catch(function() {
//     showToast('❌ Cannot connect to server.', true);
//     sendEmailBtn.disabled    = false;
//     sendEmailBtn.textContent = 'Send Email';
//   });
// });//////Backend Api of Froget Password connectivity

  forgotPasswordModal.addEventListener('click', (e) => {
    if (e.target === forgotPasswordModal) {
      closeModal();
    }
  });

  if (forgotEmailInput) {
    forgotEmailInput.addEventListener('keypress', (e) => {
      if (e.key === 'Enter') {
        e.preventDefault();
        sendEmailBtn.click();
      }
    });
    
    // Clear validation when user starts typing
    forgotEmailInput.addEventListener('input', function() {
      clearForgotValidationOnInput();
    });
    
    // Optional: Validate on blur for better UX
    forgotEmailInput.addEventListener('blur', function() {
      const email = forgotEmailInput.value.trim();
      if (email) {
        const emailValidation = validateEmail(email);
        if (!emailValidation.valid) {
          showValidationError(forgotEmailInput, forgotEmailError, emailValidation.message);
        }
      }
    });
  }

  setupEnterKey();
  loadRememberedUser();

  // Real-time validation clearing
  emailField.addEventListener('input', function() {
    clearToastOnInput();
    clearValidationOnInput();
  });
  
  passwordField.addEventListener('input', function() {
    clearToastOnInput();
    clearPasswordValidationOnInput();
  });
  
  // Optional: Real-time validation on blur
  emailField.addEventListener('blur', function() {
    const email = emailField.value.trim();
    const emailValidation = validateEmail(email);
    if (!emailValidation.valid && email) {
      showValidationError(emailField, emailError, emailValidation.message);
    } else if (!email && emailField.value !== '') {
      // If field was cleared
      clearValidationOnInput();
    }
  });
  
  passwordField.addEventListener('blur', function() {
    const password = passwordField.value;
    const passwordValidation = validatePassword(password);
    if (!passwordValidation.valid && password) {
      showValidationError(passwordField, passwordError, passwordValidation.message);
    } else if (!password && passwordField.value !== '') {
      clearPasswordValidationOnInput();
    }
  });
  // Sign Up link functionality - Open register page
    const signupLink = document.getElementById('signupLink');
    if (signupLink) {
      signupLink.addEventListener('click', function(e) {
        e.preventDefault();
        // Redirect to register.html page
        window.location.href = 'Registration.html';
      });
    }
})();

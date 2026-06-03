// =========================================
// Dynamic Admin Profile Initials
// =========================================

document.addEventListener('DOMContentLoaded', function () {

  const profileBtn = document.getElementById('profileBtn');

  if (!profileBtn) return;

  // Get logged in user
  const userData = localStorage.getItem('user');

  if (!userData) {
    profileBtn.innerText = 'NA';
    return;
  }

  const user = JSON.parse(userData);

  // Get full name
  const fullName = user.name || 'Admin User';

  // Create initials
  const initials = fullName
    .trim()
    .split(' ')
    .map(word => word.charAt(0).toUpperCase())
    .slice(0, 2)
    .join('');

  // Set initials
  profileBtn.innerText = initials;

});
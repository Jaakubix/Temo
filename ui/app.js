(function () {
  const screens = document.querySelectorAll(".screen");
  const navButtons = document.querySelectorAll("#screenNav button");

  function showScreen(id) {
    screens.forEach((screen) => {
      screen.dataset.active = screen.id === `screen-${id}` ? "true" : "false";
    });
    navButtons.forEach((btn) => {
      btn.classList.toggle("active", btn.dataset.screen === id);
    });
  }

  navButtons.forEach((btn) => {
    btn.addEventListener("click", () => showScreen(btn.dataset.screen));
  });

  document.querySelectorAll("[data-goto]").forEach((el) => {
    el.addEventListener("click", (event) => {
      const target = el.getAttribute("data-goto");
      if (!target) return;
      event.stopPropagation();
      showScreen(target);
    });
  });

  document.querySelectorAll(".tabs .tab").forEach((tab) => {
    tab.addEventListener("click", () => {
      tab.parentElement.querySelectorAll(".tab").forEach((t) => t.classList.remove("active"));
      tab.classList.add("active");
    });
  });

  const orb = document.querySelector(".shake-orb");
  const shakeBtn = document.getElementById("shakeBtn");
  const picked = document.getElementById("pickedCard");

  function simulateShake() {
    if (!orb || !picked) return;
    orb.classList.remove("shake");
    void orb.offsetWidth;
    orb.classList.add("shake");
    picked.classList.remove("hidden");
  }

  orb?.addEventListener("click", simulateShake);
  shakeBtn?.addEventListener("click", simulateShake);
})();

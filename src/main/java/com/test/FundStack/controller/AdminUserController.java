package com.test.FundStack.controller;

import com.test.FundStack.model.auth.UserResponse;
import com.test.FundStack.model.auth.UserUpsertRequest;
import com.test.FundStack.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserAdminService userAdminService;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserUpsertRequest request) {
        return ResponseEntity.ok(userAdminService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody UserUpsertRequest request) {
        return ResponseEntity.ok(userAdminService.update(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userAdminService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.ok(userAdminService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userAdminService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/revoke")
    public ResponseEntity<UserResponse> revoke(@PathVariable Long id) {
        return ResponseEntity.ok(userAdminService.revokeLogin(id));
    }

    @PostMapping("/{id}/suspend")
    public ResponseEntity<UserResponse> suspend(@PathVariable Long id,
                                                @RequestParam(defaultValue = "24") long hours) {
        return ResponseEntity.ok(userAdminService.suspendForHours(id, hours));
    }

    @PostMapping("/{id}/restore")
    public ResponseEntity<UserResponse> restore(@PathVariable Long id) {
        return ResponseEntity.ok(userAdminService.unsuspendAndUnrevoke(id));
    }
}

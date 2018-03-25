import { Component, OnDestroy, OnInit } from '@angular/core';
import { User } from '../../shared/user/user.model';
import { Subscription } from 'rxjs/Subscription';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../shared/user/user.service';
import { POSTS_PER_PAGE } from '../../app.constants';
import { Principal } from '../../shared/auth/principal.service';
import { Account } from '../../shared/user/account.model';

@Component({
  selector: 'ify-user-management',
  templateUrl: './user-management.component.html'
})
export class UserManagementComponent implements OnInit, OnDestroy {

  users: User[];
  routeData;
  page;
  previousPage;
  predicate;
  totalItems;
  postsPerPage;
  sub: Subscription;
  deleteSub: Subscription;
  currentAccount: Account;

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private principal: Principal
  ) {
    this.postsPerPage = POSTS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
      this.page = data.pagingParams.page;
      this.previousPage = data.pagingParams.page;
      this.predicate = data.pagingParams.predicate;
    });
  }

  ngOnInit() {
    this.principal.identity().then(account => {
      this.currentAccount = account;
      this.loadAll();
    });
  }

  loadAll() {
    this.sub = this.userService.query({
      page: this.page - 1,
      size: this.postsPerPage,
      sort: ['id,desc']
    }).subscribe(res => {
      this.users = res.body;
      this.totalItems = res.headers.get('X-Total-Count');
    });
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.router.navigate(['/admin/user-management'], {
        queryParams: {
          page: this.page,
          size: this.postsPerPage,
          sort: ['id,desc']
        }
      });
      this.loadAll();
    }
  }

  deleteUser(login: string) {
    this.deleteSub = this.userService.delete(login).subscribe();
    this.loadAll();
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
    this.deleteSub.unsubscribe();
  }
}
